package com.fittrack.app.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Meal categories a food entry can belong to. */
enum class MealType(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack")
}

/**
 * A single logged food item. Macros are optional (default 0) so quick entries
 * only need a name and calories. [epochDay] groups entries by calendar day
 * (java.time.LocalDate.toEpochDay) for fast per-day queries.
 */
@Entity(tableName = "food_entries")
data class FoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val calories: Int,
    val protein: Int = 0,
    val carbs: Int = 0,
    val fat: Int = 0,
    val mealType: MealType = MealType.SNACK,
    val epochDay: Long,
    val createdAt: Long = System.currentTimeMillis()
)
