package com.fittrack.app.data.seed

/**
 * Offline catalog of common foods with approximate calories and macros per
 * typical serving. Includes a dedicated set of typical Indian meals. Choosing an
 * item auto-fills the food log so calories update instantly. Values are rounded
 * estimates for a typical home serving.
 */
data class PresetFood(
    val name: String,
    val servingLabel: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val category: FoodCategory = FoodCategory.COMMON
)

enum class FoodCategory(val label: String) {
    INDIAN("Indian"),
    COMMON("Common")
}

object FoodCatalog {

    /** Typical Indian meals and dishes. */
    val indian: List<PresetFood> = listOf(
        PresetFood("Roti / Chapati", "1 piece", 110, 3, 22, 2, FoodCategory.INDIAN),
        PresetFood("Plain Paratha", "1 piece", 210, 4, 28, 9, FoodCategory.INDIAN),
        PresetFood("Aloo Paratha", "1 piece", 290, 6, 36, 13, FoodCategory.INDIAN),
        PresetFood("Naan", "1 piece", 260, 9, 45, 5, FoodCategory.INDIAN),
        PresetFood("Plain Rice", "1 cup cooked", 205, 4, 45, 0, FoodCategory.INDIAN),
        PresetFood("Jeera Rice", "1 cup", 240, 4, 45, 5, FoodCategory.INDIAN),
        PresetFood("Veg Biryani", "1 cup", 290, 7, 45, 9, FoodCategory.INDIAN),
        PresetFood("Chicken Biryani", "1 cup", 330, 15, 40, 12, FoodCategory.INDIAN),
        PresetFood("Dal (Tadka)", "1 cup", 180, 9, 25, 5, FoodCategory.INDIAN),
        PresetFood("Dal Makhani", "1 cup", 330, 11, 30, 18, FoodCategory.INDIAN),
        PresetFood("Rajma", "1 cup", 230, 12, 38, 3, FoodCategory.INDIAN),
        PresetFood("Chole", "1 cup", 270, 12, 40, 8, FoodCategory.INDIAN),
        PresetFood("Sambar", "1 cup", 150, 7, 22, 4, FoodCategory.INDIAN),
        PresetFood("Khichdi", "1 cup", 200, 8, 35, 4, FoodCategory.INDIAN),
        PresetFood("Paneer (50 g)", "50 g raw", 132, 9, 3, 10, FoodCategory.INDIAN),
        PresetFood("Paneer Butter Masala", "1 cup", 320, 12, 12, 25, FoodCategory.INDIAN),
        PresetFood("Palak Paneer", "1 cup", 280, 14, 12, 20, FoodCategory.INDIAN),
        PresetFood("Mixed Veg Curry", "1 cup", 150, 4, 18, 7, FoodCategory.INDIAN),
        PresetFood("Aloo Sabzi", "1 cup", 200, 4, 28, 8, FoodCategory.INDIAN),
        PresetFood("Chicken Curry", "1 cup", 240, 22, 8, 14, FoodCategory.INDIAN),
        PresetFood("Egg Curry", "1 cup (2 eggs)", 270, 14, 8, 20, FoodCategory.INDIAN),
        PresetFood("Idli", "2 pieces", 116, 4, 24, 1, FoodCategory.INDIAN),
        PresetFood("Plain Dosa", "1 piece", 168, 4, 28, 5, FoodCategory.INDIAN),
        PresetFood("Masala Dosa", "1 piece", 300, 6, 45, 10, FoodCategory.INDIAN),
        PresetFood("Medu Vada", "1 piece", 130, 4, 16, 6, FoodCategory.INDIAN),
        PresetFood("Upma", "1 cup", 250, 6, 40, 8, FoodCategory.INDIAN),
        PresetFood("Poha", "1 cup", 230, 5, 40, 6, FoodCategory.INDIAN),
        PresetFood("Curd / Dahi", "1 cup", 98, 8, 12, 2, FoodCategory.INDIAN),
        PresetFood("Raita", "1 cup", 110, 5, 10, 5, FoodCategory.INDIAN),
        PresetFood("Samosa", "1 piece", 260, 5, 30, 14, FoodCategory.INDIAN),
        PresetFood("Pakora", "1 cup", 300, 7, 28, 18, FoodCategory.INDIAN),
        PresetFood("Masala Chai", "1 cup", 90, 2, 12, 3, FoodCategory.INDIAN),
        PresetFood("Sweet Lassi", "1 glass", 180, 6, 30, 4, FoodCategory.INDIAN),
        PresetFood("Gulab Jamun", "1 piece", 150, 2, 25, 5, FoodCategory.INDIAN)
    )

    /** Common everyday foods. */
    val common: List<PresetFood> = listOf(
        PresetFood("Banana", "1 medium", 105, 1, 27, 0),
        PresetFood("Apple", "1 medium", 95, 0, 25, 0),
        PresetFood("Egg (boiled)", "1 large", 78, 6, 1, 5),
        PresetFood("Oatmeal", "1 cup cooked", 158, 6, 27, 3),
        PresetFood("Greek Yogurt", "170 g", 100, 17, 6, 0),
        PresetFood("Chicken Breast", "100 g cooked", 165, 31, 0, 4),
        PresetFood("Brown Rice", "1 cup cooked", 216, 5, 45, 2),
        PresetFood("Whole Wheat Bread", "1 slice", 80, 4, 14, 1),
        PresetFood("Peanut Butter", "1 tbsp", 94, 4, 3, 8),
        PresetFood("Almonds", "28 g (~23)", 164, 6, 6, 14),
        PresetFood("Milk (2%)", "1 cup", 122, 8, 12, 5),
        PresetFood("Salmon", "100 g cooked", 208, 20, 0, 13),
        PresetFood("Broccoli", "1 cup cooked", 55, 4, 11, 1),
        PresetFood("Sweet Potato", "1 medium", 112, 2, 26, 0),
        PresetFood("Avocado", "1/2 fruit", 120, 1, 6, 11),
        PresetFood("Protein Shake", "1 scoop + water", 120, 24, 3, 1),
        PresetFood("Coffee (black)", "1 cup", 2, 0, 0, 0),
        PresetFood("Orange", "1 medium", 62, 1, 15, 0),
        PresetFood("Pasta", "1 cup cooked", 221, 8, 43, 1),
        PresetFood("Tofu", "100 g", 76, 8, 2, 5),
        PresetFood("Lentils", "1 cup cooked", 230, 18, 40, 1),
        PresetFood("Cheddar Cheese", "28 g", 113, 7, 0, 9),
        PresetFood("Mixed Salad", "2 cups + dressing", 150, 3, 10, 11)
    )

    val presets: List<PresetFood> = indian + common

    /** Filter by free-text query and optional category. */
    fun search(query: String, category: FoodCategory? = null): List<PresetFood> {
        val base = when (category) {
            FoodCategory.INDIAN -> indian
            FoodCategory.COMMON -> common
            null -> presets
        }
        if (query.isBlank()) return base
        return base.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }
}
