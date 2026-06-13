package com.fittrack.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.fittrack.app.MainActivity
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import java.time.LocalDate
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

/**
 * Home-screen widget showing today's scheduled workout from the default weekly
 * plan. Tapping it opens the app on the schedule screen.
 */
class ScheduleWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as com.fittrack.app.FitTrackApp
        val today = LocalDate.now()
        val program = WorkoutScheduleSeed.programById(app.settingsRepository.selectedProgramIdOnce())
        val day = program.dayFor(today.dayOfWeek)
        val dayName = today.dayOfWeek.getDisplayName(JavaTextStyle.FULL, Locale.getDefault())
        val title = day.workout?.name ?: "Rest Day"
        val subtitle = day.workout?.let { "${it.exercises.size} exercises · ${it.estimatedMinutes} min" }
            ?: "Recover and recharge"

        provideContent {
            GlanceTheme {
                Content(program = program.name, dayName = dayName, title = title, subtitle = subtitle)
            }
        }
    }

    @Composable
    private fun Content(program: String, dayName: String, title: String, subtitle: String) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .cornerRadius(20.dp)
                .padding(14.dp)
                .clickable(actionStartActivity(scheduleIntent())),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "$dayName · $program",
                style = TextStyle(
                    color = ColorProvider(Color(0xFFF97316)),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(GlanceModifier.height(6.dp))
            Text(
                text = title,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = subtitle,
                style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 13.sp)
            )
            Spacer(GlanceModifier.height(8.dp))
            Text(
                text = "Tap to view schedule →",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    private fun scheduleIntent(): Intent =
        Intent(com.fittrack.app.FitTrackApp.instance, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(MainActivity.EXTRA_DESTINATION, MainActivity.DEST_SCHEDULE)
        }
}

class ScheduleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScheduleWidget()
}
