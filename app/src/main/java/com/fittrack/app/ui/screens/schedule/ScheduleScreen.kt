package com.fittrack.app.ui.screens.schedule

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.WorkoutDay
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import com.fittrack.app.ui.IconBadge
import com.fittrack.app.ui.Pill
import com.fittrack.app.ui.color
import com.fittrack.app.ui.icon
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/** Weekly training schedule — the app's home screen. */
@Composable
fun ScheduleScreen(
    onWorkoutClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now().dayOfWeek
    val plan = WorkoutScheduleSeed.weeklyPlan

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ScheduleHeader(today) }
        items(plan, key = { it.dayOfWeek.name }) { day ->
            DayCard(
                day = day,
                isToday = day.dayOfWeek == today,
                onClick = { day.workout?.let { onWorkoutClick(it.id) } }
            )
        }
        item { Spacer(Modifier.height(8.dp)) }
        item { FreeBanner() }
    }
}

@Composable
private fun ScheduleHeader(today: DayOfWeek) {
    val todayDay = WorkoutScheduleSeed.dayFor(today)
    Column {
        Text("Your Week", style = MaterialTheme.typography.headlineMedium)
        Text(
            "A balanced default plan — HIIT, strength, core & recovery",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(14.dp))
        TodayHighlight(todayDay)
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun TodayHighlight(day: WorkoutDay) {
    val type = day.workout?.type ?: com.fittrack.app.data.model.WorkoutType.REST
    val accent = type.color()
    Card(
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconBadge(icon = type.icon(), color = accent, sizeDp = 52)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    "TODAY",
                    style = MaterialTheme.typography.labelMedium,
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    day.workout?.name ?: "Rest Day",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    day.workout?.focus ?: "Take it easy and recover",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DayCard(day: WorkoutDay, isToday: Boolean, onClick: () -> Unit) {
    val type = day.workout?.type ?: com.fittrack.app.data.model.WorkoutType.REST
    val accent = type.color()
    val dayLabel = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = day.workout != null, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isToday) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp)
            ) {
                Text(
                    dayLabel.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isToday) accent else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
                )
                if (isToday) {
                    Spacer(Modifier.height(2.dp))
                    Box(
                        Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(accent)
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            IconBadge(icon = type.icon(), color = accent)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(day.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                if (day.workout != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Pill(text = type.label, color = accent)
                        Spacer(Modifier.width(6.dp))
                        Icon(
                            Icons.Filled.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(3.dp))
                        Text(
                            "${day.workout.estimatedMinutes} min · ${day.workout.exercises.size} exercises",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        "Active recovery — light stretching or a walk",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (day.workout != null) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open workout",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FreeBanner() {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Text(
            "100% free & offline. No account, no ads, no subscriptions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}
