package com.fittrack.app.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.data.seed.ExerciseLibrary
import com.fittrack.app.ui.color
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

private enum class Mode { PICK, BUILD }

/** Categories shown (in order) in the build-your-own picker. */
private val builderCategories = listOf(
    WorkoutType.HIIT, WorkoutType.STRENGTH, WorkoutType.CORE,
    WorkoutType.CARDIO, WorkoutType.FLEXIBILITY
)

/** Auto-pick 2 exercises from every form for a true all-forms mix. */
private fun mixAllForms(): List<String> =
    builderCategories.flatMap { cat ->
        ExerciseLibrary.all.filter { it.category == cat }.take(2)
    }.map { it.id }

/**
 * Lets the user assign a preset workout, build their own mixed workout from any
 * exercises, or set a rest day for a weekday in the custom plan.
 */
@Composable
fun CustomDayPickerDialog(
    day: DayOfWeek,
    workouts: List<Workout>,
    currentPresetId: String?,
    currentExerciseIds: List<String>,
    onPickPreset: (String?) -> Unit,
    onSaveBuilt: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
    var mode by remember { mutableStateOf(if (currentExerciseIds.isNotEmpty()) Mode.BUILD else Mode.PICK) }
    val selected = remember { mutableStateListOf<String>().also { it.addAll(currentExerciseIds) } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("$dayName plan") },
        text = {
            Column {
                // Mode switch
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ModeTab("Choose workout", mode == Mode.PICK) { mode = Mode.PICK }
                    ModeTab("Build your own", mode == Mode.BUILD) { mode = Mode.BUILD }
                }
                Spacer(Modifier.height(10.dp))

                if (mode == Mode.PICK) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 340.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        item {
                            OptionRow(
                                label = "Rest day",
                                subtitle = "No workout",
                                accent = WorkoutType.REST.color(),
                                selected = currentPresetId == null && currentExerciseIds.isEmpty(),
                                onClick = { onPickPreset(null) }
                            )
                        }
                        items(workouts, key = { it.id }) { workout ->
                            OptionRow(
                                label = workout.name,
                                subtitle = "${workout.type.label} · ${workout.estimatedMinutes} min",
                                accent = workout.type.color(),
                                selected = currentPresetId == workout.id,
                                onClick = { onPickPreset(workout.id) }
                            )
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            selected.clear()
                            selected.addAll(mixAllForms())
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Shuffle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Mix all forms (auto-pick)")
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${selected.size} selected",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(6.dp))
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        builderCategories.forEach { category ->
                            val categoryExercises = ExerciseLibrary.all.filter { it.category == category }
                            item(key = "header_${category.name}") {
                                Text(
                                    category.label.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
                                )
                            }
                            items(categoryExercises, key = { it.id }) { exercise ->
                                val isOn = selected.contains(exercise.id)
                                ExerciseCheckRow(
                                    label = exercise.name,
                                    accent = category.color(),
                                    checked = isOn,
                                    onToggle = {
                                        if (isOn) selected.remove(exercise.id) else selected.add(exercise.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (mode == Mode.BUILD) {
                TextButton(
                    enabled = selected.isNotEmpty(),
                    onClick = { onSaveBuilt(selected.toList()) }
                ) { Text("Save") }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } }
    )
}

@Composable
private fun ModeTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun OptionRow(
    label: String,
    subtitle: String,
    accent: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(accent))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Text(
                subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (selected) {
            Icon(
                Icons.Filled.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun ExerciseCheckRow(label: String, accent: Color, checked: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(if (checked) accent else MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}
