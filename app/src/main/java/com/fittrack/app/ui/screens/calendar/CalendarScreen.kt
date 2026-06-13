package com.fittrack.app.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.db.entity.WorkoutLog
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/** Calendar / history view: per-day calories logged and workouts completed. */
@Composable
fun CalendarScreen(viewModel: CalendarViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("History", style = MaterialTheme.typography.headlineMedium) }
        item {
            MonthHeader(
                title = state.month.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
                    " ${state.month.year}",
                onPrev = { viewModel.shiftMonth(-1) },
                onNext = { viewModel.shiftMonth(1) }
            )
        }
        item { Legend() }
        item {
            MonthGrid(
                state = state,
                onSelect = viewModel::selectDate
            )
        }
        item { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
        item { DayDetail(state) }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun MonthHeader(title: String, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous month")
        }
        Text(title, style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = onNext) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next month")
        }
    }
}

@Composable
private fun Legend() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        LegendItem(MaterialTheme.colorScheme.secondary, "Calories logged")
        LegendItem(MaterialTheme.colorScheme.primary, "Workout done")
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun MonthGrid(state: CalendarUiState, onSelect: (LocalDate) -> Unit) {
    val firstOfMonth = state.month.atDay(1)
    val daysInMonth = state.month.lengthOfMonth()
    // Monday = 1 ... Sunday = 7  → leading blanks before the first day.
    val leadingBlanks = firstOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value
    val today = LocalDate.now()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(Modifier.fillMaxWidth()) {
            DayOfWeek.entries.forEach { dow ->
                Text(
                    dow.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        val totalCells = leadingBlanks + daysInMonth
        val rows = (totalCells + 6) / 7
        var dayCounter = 1
        for (week in 0 until rows) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (dayCol in 0 until 7) {
                    val cellIndex = week * 7 + dayCol
                    if (cellIndex < leadingBlanks || dayCounter > daysInMonth) {
                        Box(Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val date = state.month.atDay(dayCounter)
                        DayCell(
                            date = date,
                            stat = state.stats[date],
                            isSelected = date == state.selectedDate,
                            isToday = date == today,
                            modifier = Modifier.weight(1f),
                            onClick = { onSelect(date) }
                        )
                        dayCounter++
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    stat: DayStat?,
    isSelected: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val border = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.outline
        else -> androidx.compose.ui.graphics.Color.Transparent
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                else androidx.compose.ui.graphics.Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${date.dayOfMonth}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
            )
            Spacer(Modifier.height(3.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                if ((stat?.calories ?: 0) > 0) {
                    Dot(MaterialTheme.colorScheme.secondary)
                }
                if ((stat?.workouts ?: 0) > 0) {
                    Dot(MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun Dot(color: androidx.compose.ui.graphics.Color) {
    Box(Modifier.size(6.dp).clip(CircleShape).background(color))
}

@Composable
private fun DayDetail(state: CalendarUiState) {
    val date = state.selectedDate
    val label = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()) +
        ", ${date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${date.dayOfMonth}"
    Column {
        Text(label, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "${state.selectedCalories} / ${state.calorieGoal} kcal",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.FitnessCenter,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (state.selectedWorkouts.isEmpty()) "No workouts logged"
                else "${state.selectedWorkouts.size} workout${if (state.selectedWorkouts.size > 1) "s" else ""} completed",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (state.selectedWorkouts.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            state.selectedWorkouts.forEach { log -> WorkoutLogRow(log) }
        }
    }
}

@Composable
private fun WorkoutLogRow(log: WorkoutLog) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(log.workoutName, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${log.type} · ${log.durationMinutes} min",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
