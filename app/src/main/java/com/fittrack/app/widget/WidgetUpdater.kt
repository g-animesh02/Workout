package com.fittrack.app.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll

/**
 * Pushes fresh data into the home-screen widgets. Called whenever food or
 * settings change so the widgets stay in sync with the app.
 */
class WidgetUpdater(private val context: Context) {

    suspend fun refreshAll() {
        runCatching { FoodWidget().updateAll(context) }
        runCatching { ScheduleWidget().updateAll(context) }
    }

    suspend fun hasAnyWidget(): Boolean {
        val manager = GlanceAppWidgetManager(context)
        val food = manager.getGlanceIds(FoodWidget::class.java)
        val schedule = manager.getGlanceIds(ScheduleWidget::class.java)
        return food.isNotEmpty() || schedule.isNotEmpty()
    }
}
