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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.seed.LevelScaling
import com.fittrack.app.data.seed.WorkoutStep
import com.fittrack.app.ui.color
import kotlinx.coroutines.delay

private enum class Phase { WORK, REST }

/**
 * Guided workout runner. The session is a single, level-scaled pass through the
 * exercises (longer time, more reps and more exercises at higher levels — not
 * extra rounds). Time, rest and reps can all be adjusted live during execution.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    workout: Workout,
    level: Difficulty,
    onExit: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember(workout.id, level) { LevelScaling.plan(workout.exercises, level) }
    if (steps.isEmpty()) {
        LaunchedEffect(Unit) { onExit() }
        return
    }

    var index by remember { mutableIntStateOf(0) }
    var phase by remember { mutableStateOf(Phase.WORK) }
    var secondsLeft by remember { mutableIntStateOf(steps[0].workSeconds) }
    var running by remember { mutableStateOf(true) }
    var finished by remember { mutableStateOf(false) }
    // When set, after the current REST ends we resume WORK on the same exercise
    // with this many seconds (used by the "add rest" break button).
    var resumeWork by remember { mutableStateOf<Int?>(null) }
    val repsOverride = remember { mutableStateMapOf<Int, Int>() }

    fun step() = steps[index]
    fun isLast() = index >= steps.lastIndex

    fun startWork(i: Int) {
        index = i
        phase = Phase.WORK
        secondsLeft = steps[i].workSeconds
        resumeWork = null
    }

    fun advance() {
        when (phase) {
            Phase.WORK -> {
                if (isLast()) {
                    finished = true
                    running = false
                } else {
                    phase = Phase.REST
                    secondsLeft = step().restSeconds
                }
            }
            Phase.REST -> {
                val resume = resumeWork
                if (resume != null) {
                    phase = Phase.WORK
                    secondsLeft = resume
                    resumeWork = null
                } else {
                    startWork(index + 1)
                }
            }
        }
    }

    fun next() {
        if (isLast()) { finished = true; running = false } else startWork(index + 1)
    }

    fun previous() {
        startWork(if (index > 0) index - 1 else 0)
    }

    fun addTime(delta: Int) {
        secondsLeft = (secondsLeft + delta).coerceAtLeast(1)
    }

    fun addRest() {
        if (phase == Phase.WORK) {
            resumeWork = secondsLeft
            phase = Phase.REST
            secondsLeft = 30
        } else {
            secondsLeft += 15
        }
    }

    fun baseReps(): Int? =
        step().reps?.let { Regex("\\d+").find(it)?.value?.toIntOrNull() }

    fun adjustReps(delta: Int) {
        val current = repsOverride[index] ?: baseReps() ?: return
        repsOverride[index] = (current + delta).coerceAtLeast(1)
    }

    LaunchedEffect(running, finished) {
        if (!running || finished) return@LaunchedEffect
        while (true) {
            delay(1000L)
            if (secondsLeft > 1) secondsLeft -= 1 else advance()
            if (finished) break
        }
    }

    LaunchedEffect(finished) { if (finished) onCompleted() }

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
                step = step(),
                phase = phase,
                secondsLeft = secondsLeft,
                position = index + 1,
                total = steps.size,
                running = running,
                nextExerciseName = if (resumeWork != null) step().exercise.name
                    else steps.getOrNull(index + 1)?.exercise?.name ?: step().exercise.name,
                repsDisplay = repsOverride[index]?.let { "$it reps" } ?: step().reps,
                onToggle = { running = !running },
                onNext = ::next,
                onPrev = ::previous,
                onAddTime = { addTime(15) },
                onSubTime = { addTime(-15) },
                onAddRest = ::addRest,
                onRepsUp = { adjustReps(1) },
                onRepsDown = { adjustReps(-1) },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun RunningView(
    step: WorkoutStep,
    phase: Phase,
    secondsLeft: Int,
    position: Int,
    total: Int,
    running: Boolean,
    nextExerciseName: String,
    repsDisplay: String?,
    onToggle: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onAddTime: () -> Unit,
    onSubTime: () -> Unit,
    onAddRest: () -> Unit,
    onRepsUp: () -> Unit,
    onRepsDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = step.exercise.category.color()
    val isRest = phase == Phase.REST
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Exercise $position of $total",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        Text(
            if (isRest) "REST" else step.exercise.category.label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isRest) MaterialTheme.colorScheme.onSurfaceVariant else accent,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background((if (isRest) MaterialTheme.colorScheme.onSurfaceVariant else accent).copy(alpha = 0.10f))
            )
            Text(
                formatTime(secondsLeft),
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Live time edit
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SmallPill("−15s", onSubTime)
            SmallPill("+15s", onAddTime)
            SmallPill(if (isRest) "+15s rest" else "+ Rest", onAddRest)
        }

        Spacer(Modifier.height(18.dp))
        Text(
            if (isRest) "Up next: $nextExerciseName" else step.exercise.name,
            style = MaterialTheme.typography.headlineSmall
        )
        if (!isRest && repsDisplay != null) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                RoundIcon(Icons.Filled.Remove, "Fewer reps", onRepsDown)
                Text(repsDisplay, style = MaterialTheme.typography.titleLarge)
                RoundIcon(Icons.Filled.Add, "More reps", onRepsUp)
            }
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
        Spacer(Modifier.height(10.dp))
        Text(
            if (running) "Adjust time and reps anytime" else "Paused",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SmallPill(label: String, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, contentPadding = PaddingValuesSmall) {
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

private val PaddingValuesSmall = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)

@Composable
private fun RoundIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, desc: String, onClick: () -> Unit) {
    FilledIconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp),
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Icon(icon, contentDescription = desc, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
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
