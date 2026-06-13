package com.fittrack.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
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
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.compose.ui.graphics.Color
import com.fittrack.app.FitTrackApp
import com.fittrack.app.MainActivity
import com.fittrack.app.QuickAddActivity
import java.time.LocalDate

/**
 * Home-screen widget showing today's calorie total versus the daily goal, with
 * a one-tap "+ Add food" button that launches a lightweight quick-add screen.
 */
class FoodWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val app = context.applicationContext as FitTrackApp
        val today = LocalDate.now()
        val calories = app.foodRepository.caloriesForDayOnce(today)
        val goal = app.settingsRepository.currentGoalOnce()

        provideContent {
            GlanceTheme {
                FoodWidgetContent(calories = calories, goal = goal)
            }
        }
    }

    @Composable
    private fun FoodWidgetContent(calories: Int, goal: Int) {
        val remaining = (goal - calories).coerceAtLeast(0)
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .cornerRadius(20.dp)
                .padding(14.dp)
                .clickable(actionStartActivity(mainIntent())),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Today's Calories",
                style = TextStyle(
                    color = GlanceTheme.colors.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(GlanceModifier.height(4.dp))
            Text(
                text = "$calories",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "of $goal kcal · $remaining left",
                style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 12.sp)
            )
            Spacer(GlanceModifier.height(10.dp))
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(ColorProvider(Color(0xFF10B981)))
                    .cornerRadius(12.dp)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .clickable(actionStartActivity(quickAddIntent())),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "+ Add food",
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }

    private fun quickAddIntent(): Intent =
        Intent(FitTrackApp.instance, QuickAddActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

    private fun mainIntent(): Intent =
        Intent(FitTrackApp.instance, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(MainActivity.EXTRA_DESTINATION, MainActivity.DEST_FOOD)
        }
}

class FoodWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FoodWidget()
}
