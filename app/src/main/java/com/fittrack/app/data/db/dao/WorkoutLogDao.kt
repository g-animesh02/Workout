package com.fittrack.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fittrack.app.data.db.entity.WorkoutLog
import kotlinx.coroutines.flow.Flow

/** Aggregated per-day counts used to render calendar markers. */
data class DayCount(val epochDay: Long, val count: Int)

@Dao
interface WorkoutLogDao {

    @Insert
    suspend fun insert(log: WorkoutLog): Long

    @Delete
    suspend fun delete(log: WorkoutLog)

    @Query("SELECT * FROM workout_logs WHERE epochDay = :epochDay ORDER BY completedAt DESC")
    fun observeLogsForDay(epochDay: Long): Flow<List<WorkoutLog>>

    @Query(
        "SELECT epochDay AS epochDay, COUNT(*) AS count FROM workout_logs " +
            "WHERE epochDay BETWEEN :start AND :end GROUP BY epochDay"
    )
    fun observeDailyCounts(start: Long, end: Long): Flow<List<DayCount>>

    @Query("SELECT COUNT(*) FROM workout_logs WHERE epochDay = :epochDay")
    suspend fun countForDayOnce(epochDay: Long): Int
}
