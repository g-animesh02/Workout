package com.fittrack.app.data.seed

/**
 * A small offline catalog of common foods with approximate calories and macros
 * per typical serving. Used to power quick-add suggestions so users can log a
 * meal in one tap without any internet connection. Values are rounded estimates.
 */
data class PresetFood(
    val name: String,
    val servingLabel: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int
)

object FoodCatalog {
    val presets: List<PresetFood> = listOf(
        PresetFood("Banana", "1 medium", 105, 1, 27, 0),
        PresetFood("Apple", "1 medium", 95, 0, 25, 0),
        PresetFood("Egg (boiled)", "1 large", 78, 6, 1, 5),
        PresetFood("Oatmeal", "1 cup cooked", 158, 6, 27, 3),
        PresetFood("Greek Yogurt", "170 g", 100, 17, 6, 0),
        PresetFood("Chicken Breast", "100 g cooked", 165, 31, 0, 4),
        PresetFood("White Rice", "1 cup cooked", 205, 4, 45, 0),
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

    fun search(query: String): List<PresetFood> {
        if (query.isBlank()) return presets
        return presets.filter { it.name.contains(query.trim(), ignoreCase = true) }
    }
}
