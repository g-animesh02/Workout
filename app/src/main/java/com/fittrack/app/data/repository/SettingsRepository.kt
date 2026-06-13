package com.fittrack.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "fittrack_settings")

/** Stores lightweight user preferences such as the daily calorie goal. */
class SettingsRepository(private val context: Context) {

    private val calorieGoalKey = intPreferencesKey("daily_calorie_goal")

    val dailyCalorieGoal: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[calorieGoalKey] ?: DEFAULT_CALORIE_GOAL }

    suspend fun setDailyCalorieGoal(goal: Int) {
        context.dataStore.edit { prefs ->
            prefs[calorieGoalKey] = goal.coerceIn(800, 6000)
        }
    }

    suspend fun currentGoalOnce(): Int =
        runCatching { dailyCalorieGoal.first() }.getOrDefault(DEFAULT_CALORIE_GOAL)

    companion object {
        const val DEFAULT_CALORIE_GOAL = 2000
    }
}
