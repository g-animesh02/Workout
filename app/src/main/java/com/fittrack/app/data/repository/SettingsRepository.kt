package com.fittrack.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek

private val Context.dataStore by preferencesDataStore(name = "fittrack_settings")

/**
 * Stores lightweight user preferences: daily calorie goal, the selected weekly
 * program, the chosen difficulty level, and a fully custom weekly schedule.
 */
class SettingsRepository(private val context: Context) {

    private val calorieGoalKey = intPreferencesKey("daily_calorie_goal")
    private val programIdKey = stringPreferencesKey("selected_program_id")
    private val levelKey = stringPreferencesKey("difficulty_level")
    private val customScheduleKey = stringPreferencesKey("custom_schedule")
    private val customWorkoutsKey = stringPreferencesKey("custom_day_workouts")

    // --- Calorie goal ---

    val dailyCalorieGoal: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[calorieGoalKey] ?: DEFAULT_CALORIE_GOAL }

    suspend fun setDailyCalorieGoal(goal: Int) {
        context.dataStore.edit { prefs -> prefs[calorieGoalKey] = goal.coerceIn(800, 6000) }
    }

    suspend fun currentGoalOnce(): Int =
        runCatching { dailyCalorieGoal.first() }.getOrDefault(DEFAULT_CALORIE_GOAL)

    // --- Selected program ---

    val selectedProgramId: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[programIdKey] ?: WorkoutScheduleSeed.DEFAULT_PROGRAM_ID }

    suspend fun setSelectedProgram(id: String) {
        context.dataStore.edit { prefs -> prefs[programIdKey] = id }
    }

    suspend fun selectedProgramIdOnce(): String =
        runCatching { selectedProgramId.first() }.getOrDefault(WorkoutScheduleSeed.DEFAULT_PROGRAM_ID)

    // --- Difficulty level ---

    val selectedLevel: Flow<Difficulty> = context.dataStore.data
        .map { prefs ->
            runCatching { Difficulty.valueOf(prefs[levelKey] ?: "") }
                .getOrDefault(Difficulty.INTERMEDIATE)
        }

    suspend fun setLevel(level: Difficulty) {
        context.dataStore.edit { prefs -> prefs[levelKey] = level.name }
    }

    // --- Custom weekly schedule ---

    /** Map of each weekday to a workout id, or null for a rest day. */
    val customSchedule: Flow<Map<DayOfWeek, String?>> = context.dataStore.data
        .map { prefs -> decodeSchedule(prefs[customScheduleKey]) }

    suspend fun setCustomDay(day: DayOfWeek, workoutId: String?) {
        context.dataStore.edit { prefs ->
            val current = decodeSchedule(prefs[customScheduleKey]).toMutableMap()
            current[day] = workoutId
            prefs[customScheduleKey] = encodeSchedule(current)
        }
    }

    suspend fun customScheduleOnce(): Map<DayOfWeek, String?> =
        runCatching { customSchedule.first() }.getOrDefault(defaultCustomSchedule())

    /** Per-day user-built workouts as ordered lists of exercise ids. Days absent
     * here fall back to the preset workout chosen in [customSchedule]. */
    val customDayWorkouts: Flow<Map<DayOfWeek, List<String>>> = context.dataStore.data
        .map { prefs -> decodeWorkouts(prefs[customWorkoutsKey]) }

    suspend fun setCustomDayExercises(day: DayOfWeek, exerciseIds: List<String>) {
        context.dataStore.edit { prefs ->
            val current = decodeWorkouts(prefs[customWorkoutsKey]).toMutableMap()
            if (exerciseIds.isEmpty()) current.remove(day) else current[day] = exerciseIds
            prefs[customWorkoutsKey] = encodeWorkouts(current)
        }
    }

    private fun encodeWorkouts(map: Map<DayOfWeek, List<String>>): String =
        map.entries.filter { it.value.isNotEmpty() }
            .joinToString(";") { "${it.key.name}=${it.value.joinToString(",")}" }

    private fun decodeWorkouts(raw: String?): Map<DayOfWeek, List<String>> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(";").mapNotNull { entry ->
            val parts = entry.split("=", limit = 2)
            if (parts.size != 2) return@mapNotNull null
            val day = runCatching { DayOfWeek.valueOf(parts[0]) }.getOrNull() ?: return@mapNotNull null
            val ids = parts[1].split(",").map { it.trim() }.filter { it.isNotBlank() }
            if (ids.isEmpty()) null else day to ids
        }.toMap()
    }

    private fun encodeSchedule(map: Map<DayOfWeek, String?>): String =
        DayOfWeek.entries.joinToString(";") { day -> "${day.name}=${map[day] ?: REST}" }

    private fun decodeSchedule(raw: String?): Map<DayOfWeek, String?> {
        if (raw.isNullOrBlank()) return defaultCustomSchedule()
        val parsed = raw.split(";").mapNotNull { entry ->
            val parts = entry.split("=", limit = 2)
            if (parts.size != 2) return@mapNotNull null
            val day = runCatching { DayOfWeek.valueOf(parts[0]) }.getOrNull() ?: return@mapNotNull null
            val value = if (parts[1] == REST) null else parts[1]
            day to value
        }.toMap()
        // Ensure every day is present, falling back to the default.
        val base = defaultCustomSchedule().toMutableMap()
        parsed.forEach { (day, value) -> base[day] = value }
        return base
    }

    /** A sensible starting point: mirror the balanced program. */
    private fun defaultCustomSchedule(): Map<DayOfWeek, String?> =
        WorkoutScheduleSeed.balanced.days.associate { it.dayOfWeek to it.workout?.id }

    companion object {
        const val DEFAULT_CALORIE_GOAL = 2000
        private const val REST = "REST"
    }
}
