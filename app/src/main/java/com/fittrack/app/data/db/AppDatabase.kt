package com.fittrack.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.fittrack.app.data.db.dao.FoodDao
import com.fittrack.app.data.db.entity.FoodEntry
import com.fittrack.app.data.db.entity.MealType

class Converters {
    @TypeConverter
    fun mealTypeToString(value: MealType): String = value.name

    @TypeConverter
    fun stringToMealType(value: String): MealType =
        runCatching { MealType.valueOf(value) }.getOrDefault(MealType.SNACK)
}

@Database(entities = [FoodEntry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fittrack.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
