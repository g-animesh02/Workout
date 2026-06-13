package com.fittrack.app.data.repository

import com.fittrack.app.data.db.dao.FoodDao
import com.fittrack.app.data.db.entity.FoodEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Coordinates access to logged food entries. */
class FoodRepository(private val foodDao: FoodDao) {

    fun entriesForDay(date: LocalDate): Flow<List<FoodEntry>> =
        foodDao.observeEntriesForDay(date.toEpochDay())

    fun caloriesForDay(date: LocalDate): Flow<Int> =
        foodDao.observeCaloriesForDay(date.toEpochDay())

    suspend fun caloriesForDayOnce(date: LocalDate): Int =
        foodDao.caloriesForDayOnce(date.toEpochDay())

    suspend fun recentEntries(date: LocalDate, limit: Int = 3): List<FoodEntry> =
        foodDao.recentEntriesForDay(date.toEpochDay(), limit)

    suspend fun addEntry(entry: FoodEntry): Long = foodDao.insert(entry)

    suspend fun deleteEntry(entry: FoodEntry) = foodDao.delete(entry)

    suspend fun deleteById(id: Long) = foodDao.deleteById(id)
}
