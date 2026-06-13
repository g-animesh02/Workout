package com.fittrack.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A record that a workout was completed on a given day. Powers the calendar /
 * history view. [epochDay] is LocalDate.toEpochDay() for fast per-day queries.
 */
@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val epochDay: Long,
    val workoutId: String,
    val workoutName: String,
    val type: String,
    val durationMinutes: Int = 0,
    val completedAt: Long = System.currentTimeMillis()
)
