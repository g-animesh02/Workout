package com.fittrack.app.ui.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fittrack.app.data.model.Exercise
import com.fittrack.app.data.model.Workout
import com.fittrack.app.ui.color
import kotlinx.coroutines.delay

private enum class Phase { WORK, REST }

private const val DEFAULT_WORK = 45
private const val DEFAULT_REST = 20

/**
 * Guided workout runner with start / pause / stop, auto-advancing through each
 * exercise and round with a countdown timer. Logs the workout when finished.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    workout: Workout,
    rounds: Int,
    onExit: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val exercises = workout.exercises
    val totalRounds = rounds.coerceAtLeast(1)

    var round by remember { mutableIntStateOf(1) }
    var index by remember { mutableIntStateOf(0) }
    var phase by remember { mutableStateOf(Phase.WORK) }
    var secondsLeft by remember { mutableIntStateOf(exercises.firstOrNull()?.workSeconds ?: DEFAULT_WORK) }
    var running by remember { mutableStateOf(true) }
    var finished by remember { mutableStateOf(false) }

    fun workSecondsFor(i: Int) = exercises[i].workSeconds ?: DEFAULT_WORK
    fun restSecondsFor(i: Int) = exercises[i].restSeconds ?: DEFAULT_REST

    fun isLastExercise() = round >= totalRounds && index >= exercises.lastIndex

    fun goToExercise(newIndex: Int, newRound: Int) {
        round = newRound
        index = newIndex
        phase = Phase.WORK
        secondsLeft = workSecondsFor(newIndex)
    }

    fun advance() {
        when (phase) {
            Phase.WORK -> {
                if (isLastExercise()) {
                    finished = true
                    running = false
                } else {
                    phase = Phase.REST
                    secondsLeft = restSecondsFor(index)
                }
            }
            Phase.REST -> {
                if (index < exercises.lastIndex) goToExercise(index + 1, round)
                else goToExercise(0, round + 1)
            }
        }
    }

    fun next() {
        if (isLastExercise()) { finished = true; running = false }
        else if (index < exercises.lastIndex) goToExercise(index + 1, round)
        else goToExercise(0, round + 1)
    }

    fun previous() {
        if (index > 0) goToExercise(index - 1, round)
        else if (round > 1) goToExercise(exercises.lastIndex, round - 1)
        else goToExercise(0, 1)
    }

    // Ticking timer — restarts whenever running toggles.
    LaunchedEffect(running, finished) {
        if (!running || finished) return@LaunchedEffect
        while (true) {
            delay(1000L)
            if (secondsLeft > 1) secondsLeft -= 1 else advance()
            if (finished) break
        }
    }

    // Log completion once.
    LaunchedEffect(finished) {
        if (finished) onCompleted()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(workout.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onExit) {
                        Icon(Icons.Filled.Close, contentDescription = "Stop and exit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        if (finished) {
            CompletionView(workout = workout, onDone = onExit, modifier = Modifier.padding(padding))
        } else {
            RunningView(
                exercise = exercises[index],
                phase = phase,
                secondsLeft = secondsLeft,
                round = round,
                totalRounds = totalRounds,
                position = index + 1,
                total = exercises.size,
                running = running,
                onToggle = { running = !running },
                onNext = ::next,
                onPrev = ::previous,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun RunningView(
    exercise: Exercise,
    phase: Phase,
    secondsLeft: Int,
    round: Int,
    totalRounds: Int,
    position: Int,
    total: Int,
    running: Boolean,
    onToggle: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = exercise.category.color()
    val isRest = phase == Phase.REST
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Round $round of $totalRounds · Exercise $position of $total",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isRest) "REST" else exercise.category.label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isRest) MaterialTheme.colorScheme.onSurfaceVariant else accent,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background((if (isRest) MaterialTheme.colorScheme.onSurfaceVariant else accent).copy(alpha = 0.10f))
            )
            Text(
                formatTime(secondsLeft),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(28.dp))

        Text(
            if (isRest) "Up next" else exercise.name,
            style = MaterialTheme.typography.headlineMedium
        )
        if (!isRest && exercise.dosage.isNotBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(
                exercise.dosage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (isRest) {
            Spacer(Modifier.height(4.dp))
            Text(exercise.name, style = MaterialTheme.typography.titleMedium, color = accent)
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrev) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Previous", modifier = Modifier.size(34.dp))
            }
            FilledIconButton(
                onClick = onToggle,
                modifier = Modifier.size(76.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = accent)
            ) {
                Icon(
                    if (running) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (running) "Pause" else "Resume",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = onNext) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Skip", modifier = Modifier.size(34.dp))
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            if (running) "Tap pause to take a break" else "Paused",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CompletionView(workout: Workout, onDone: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Done,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(52.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text("Workout complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            "${workout.name} logged to your calendar. Great work!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(28.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text("Done")
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}
