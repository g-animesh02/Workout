package com.fittrack.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.fittrack.app.data.db.entity.FoodEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Insert
    suspend fun insert(entry: FoodEntry): Long

    @Delete
    suspend fun delete(entry: FoodEntry)

    @Query("DELETE FROM food_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM food_entries WHERE epochDay = :epochDay ORDER BY createdAt ASC")
    fun observeEntriesForDay(epochDay: Long): Flow<List<FoodEntry>>

    @Query("SELECT COALESCE(SUM(calories), 0) FROM food_entries WHERE epochDay = :epochDay")
    fun observeCaloriesForDay(epochDay: Long): Flow<Int>

    /** Synchronous reads used by the home-screen widget (off the main thread). */
    @Query("SELECT COALESCE(SUM(calories), 0) FROM food_entries WHERE epochDay = :epochDay")
    suspend fun caloriesForDayOnce(epochDay: Long): Int

    @Query("SELECT * FROM food_entries WHERE epochDay = :epochDay ORDER BY createdAt DESC LIMIT :limit")
    suspend fun recentEntriesForDay(epochDay: Long, limit: Int): List<FoodEntry>
}
