package com.fittrack.app.ui.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.WeeklyProgram
import com.fittrack.app.data.model.WorkoutDay
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.ui.color
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/** Minimalist weekly schedule with program + level selection and custom editing. */
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel,
    onWorkoutClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val today = LocalDate.now().dayOfWeek
    var editingDay by remember { mutableStateOf<DayOfWeek?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text("Your week", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            ChipRow(
                items = state.chips.map { it.id to it.name },
                selectedId = state.selectedId,
                onSelect = viewModel::selectProgram
            )
        }

        item {
            LabeledChips(
                label = "Level",
                items = Difficulty.entries.map { it.name to it.label },
                selectedId = state.level.name,
                onSelect = { viewModel.setLevel(Difficulty.valueOf(it)) }
            )
        }

        item { ProgramSummary(state.selected, state.level.label, state.rounds) }

        item { TodayCard(state.selected.dayFor(today), state.level.label, state.rounds) }

        item {
            WeekList(
                program = state.selected,
                today = today,
                isCustom = state.isCustom,
                onDayClick = { day ->
                    if (state.isCustom) editingDay = day.dayOfWeek
                    else day.workout?.let { onWorkoutClick(it.id) }
                }
            )
        }
        item { Spacer(Modifier.height(8.dp)) }
    }

    val day = editingDay
    if (day != null) {
        val current = state.selected.dayFor(day).workout
        val isBuilt = current?.id?.startsWith("custom_") == true
        CustomDayPickerDialog(
            day = day,
            workouts = state.allWorkouts,
            currentPresetId = if (isBuilt) null else current?.id,
            currentExerciseIds = if (isBuilt) current!!.exercises.map { it.id } else emptyList(),
            onPickPreset = { workoutId ->
                viewModel.setCustomDay(day, workoutId)
                editingDay = null
            },
            onSaveBuilt = { ids ->
                viewModel.setCustomDayExercises(day, ids)
                editingDay = null
            },
            onDismiss = { editingDay = null }
        )
    }
}

@Composable
private fun ChipRow(items: List<Pair<String, String>>, selectedId: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (id, label) -> SelectChip(label, id == selectedId) { onSelect(id) } }
    }
}

@Composable
private fun LabeledChips(
    label: String,
    items: List<Pair<String, String>>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items.forEach { (id, text) -> SelectChip(text, id == selectedId) { onSelect(id) } }
        }
    }
}

@Composable
private fun SelectChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
    Surface(
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, border, RoundedCornerShape(50))
            .clickable(onClick = onClick)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ProgramSummary(program: WeeklyProgram, levelLabel: String, rounds: Int) {
    Column {
        Text(program.tagline, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(2.dp))
        Text(
            program.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "${program.trainingDays} days/week · $levelLabel · $rounds rounds per workout",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TodayCard(day: WorkoutDay, levelLabel: String, rounds: Int) {
    val accent = (day.workout?.type ?: WorkoutType.REST).color()
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "TODAY",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(day.workout?.name ?: "Rest Day", style = MaterialTheme.typography.titleLarge)
                Text(
                    day.workout?.focus ?: "Recover and recharge",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (day.workout != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "$levelLabel · $rounds rounds",
                        style = MaterialTheme.typography.labelMedium,
                        color = accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun WeekList(
    program: WeeklyProgram,
    today: DayOfWeek,
    isCustom: Boolean,
    onDayClick: (WorkoutDay) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            program.days.forEachIndexed { index, day ->
                DayRow(day, day.dayOfWeek == today, isCustom, onClick = { onDayClick(day) })
                if (index < program.days.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun DayRow(day: WorkoutDay, isToday: Boolean, isCustom: Boolean, onClick: () -> Unit) {
    val accent = (day.workout?.type ?: WorkoutType.REST).color()
    val dayLabel = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isCustom || day.workout != null, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            dayLabel.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.width(38.dp)
        )
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (day.workout != null) accent else MaterialTheme.colorScheme.outline)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(day.title, style = MaterialTheme.typography.titleMedium)
            Text(
                if (day.workout != null) "${day.workout.type.label} · ${day.workout.estimatedMinutes} min"
                else "Rest day",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            if (isCustom) Icons.Filled.Edit else Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = if (isCustom) "Edit day" else "Open workout",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}
