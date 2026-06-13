package com.fittrack.app.data.repository

import com.fittrack.app.data.db.dao.DayCalories
import com.fittrack.app.data.db.dao.DayCount
import com.fittrack.app.data.db.dao.FoodDao
import com.fittrack.app.data.db.dao.WorkoutLogDao
import com.fittrack.app.data.db.entity.WorkoutLog
import com.fittrack.app.data.model.Workout
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Provides the data behind the calendar / history view: per-day calories and
 * completed workouts, plus the ability to log a finished workout.
 */
class HistoryRepository(
    private val workoutLogDao: WorkoutLogDao,
    private val foodDao: FoodDao
) {

    suspend fun logWorkout(
        workout: Workout,
        durationMinutes: Int = workout.estimatedMinutes,
        date: LocalDate = LocalDate.now()
    ) {
        workoutLogDao.insert(
            WorkoutLog(
                epochDay = date.toEpochDay(),
                workoutId = workout.id,
                workoutName = workout.name,
                type = workout.type.label,
                durationMinutes = durationMinutes
            )
        )
    }

    fun logsForDay(date: LocalDate): Flow<List<WorkoutLog>> =
        workoutLogDao.observeLogsForDay(date.toEpochDay())

    fun dailyWorkoutCounts(start: LocalDate, end: LocalDate): Flow<List<DayCount>> =
        workoutLogDao.observeDailyCounts(start.toEpochDay(), end.toEpochDay())

    fun dailyCalories(start: LocalDate, end: LocalDate): Flow<List<DayCalories>> =
        foodDao.observeDailyCalories(start.toEpochDay(), end.toEpochDay())

    suspend fun deleteLog(log: WorkoutLog) = workoutLogDao.delete(log)
}
