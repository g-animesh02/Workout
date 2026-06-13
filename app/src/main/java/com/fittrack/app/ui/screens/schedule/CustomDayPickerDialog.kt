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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.ui.color
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/** Lets the user assign a workout (or a rest day) to a weekday in the custom plan. */
@Composable
fun CustomDayPickerDialog(
    day: DayOfWeek,
    workouts: List<Workout>,
    currentId: String?,
    onPick: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault())
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("$dayName workout") },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                item {
                    OptionRow(
                        label = "Rest day",
                        subtitle = "No workout",
                        accent = WorkoutType.REST.color(),
                        selected = currentId == null,
                        onClick = { onPick(null) }
                    )
                }
                items(workouts, key = { it.id }) { workout ->
                    OptionRow(
                        label = workout.name,
                        subtitle = "${workout.type.label} · ${workout.estimatedMinutes} min",
                        accent = workout.type.color(),
                        selected = currentId == workout.id,
                        onClick = { onPick(workout.id) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } }
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
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
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
