package com.fittrack.app.ui.screens.workout

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.Exercise
import com.fittrack.app.data.model.Workout
import com.fittrack.app.ui.IconBadge
import com.fittrack.app.ui.Pill
import com.fittrack.app.ui.color
import com.fittrack.app.ui.icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    workout: Workout,
    level: com.fittrack.app.data.model.Difficulty,
    onBack: () -> Unit,
    onExerciseClick: (String) -> Unit,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = workout.type.color()
    val steps = androidx.compose.runtime.remember(workout.id, level) {
        com.fittrack.app.data.seed.LevelScaling.plan(workout.exercises, level)
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(workout.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { WorkoutHeader(workout, level, accent) }
            item {
                androidx.compose.material3.Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = accent)
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Start workout")
                }
            }
            item {
                Text(
                    "Level: ${level.label} — ${com.fittrack.app.data.seed.LevelScaling.summary(level)}. " +
                        "Adjust time and reps live during the session.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                Text(
                    "Exercises (${steps.size}) · ${level.label} · ~${com.fittrack.app.data.seed.LevelScaling.planMinutes(steps)} min",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            itemsIndexed(steps, key = { index, _ -> index }) { index, step ->
                ExerciseRow(
                    index = index + 1,
                    exercise = step.exercise,
                    dosage = if (step.reps != null) "${step.reps} · ${step.workSeconds}s" else "${step.workSeconds}s work",
                    onClick = { onExerciseClick(step.exercise.id) }
                )
            }
        }
    }
}

@Composable
private fun WorkoutHeader(workout: Workout, level: com.fittrack.app.data.model.Difficulty, accent: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(icon = workout.type.icon(), color = accent, sizeDp = 52)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(workout.name, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        workout.focus,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill(text = workout.type.label, color = accent, leadingIcon = workout.type.icon())
                Pill(text = level.label, color = MaterialTheme.colorScheme.secondary)
                Pill(
                    text = "${com.fittrack.app.data.seed.LevelScaling.targetMinutes(level)} min",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(workout.description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ExerciseRow(index: Int, exercise: Exercise, dosage: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(exercise.category.color().copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "$index",
                    style = MaterialTheme.typography.labelLarge,
                    color = exercise.category.color(),
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(exercise.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text(
                    dosage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View exercise",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
