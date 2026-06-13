package com.fittrack.app.ui.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fittrack.app.data.db.entity.FoodEntry
import com.fittrack.app.data.db.entity.MealType
import com.fittrack.app.data.repository.FoodRepository
import com.fittrack.app.data.repository.SettingsRepository
import com.fittrack.app.widget.WidgetUpdater
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FoodUiState(
    val date: LocalDate = LocalDate.now(),
    val entries: List<FoodEntry> = emptyList(),
    val totalCalories: Int = 0,
    val totalProtein: Int = 0,
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val calorieGoal: Int = SettingsRepository.DEFAULT_CALORIE_GOAL
) {
    val remaining: Int get() = calorieGoal - totalCalories
    val progress: Float get() = if (calorieGoal <= 0) 0f else (totalCalories.toFloat() / calorieGoal).coerceIn(0f, 1f)
}

@OptIn(ExperimentalCoroutinesApi::class)
class FoodViewModel(
    private val foodRepository: FoodRepository,
    private val settingsRepository: SettingsRepository,
    private val widgetUpdater: WidgetUpdater
) : ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())

    private val entriesFlow = selectedDate.flatMapLatest { date ->
        foodRepository.entriesForDay(date)
    }

    val uiState: StateFlow<FoodUiState> =
        combine(
            selectedDate,
            entriesFlow,
            settingsRepository.dailyCalorieGoal
        ) { date, entries, goal ->
            FoodUiState(
                date = date,
                entries = entries,
                totalCalories = entries.sumOf { it.calories },
                totalProtein = entries.sumOf { it.protein },
                totalCarbs = entries.sumOf { it.carbs },
                totalFat = entries.sumOf { it.fat },
                calorieGoal = goal
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FoodUiState())

    fun addEntry(
        name: String,
        calories: Int,
        protein: Int = 0,
        carbs: Int = 0,
        fat: Int = 0,
        mealType: MealType = MealType.SNACK
    ) {
        val cleanName = name.trim().ifBlank { "Food" }
        viewModelScope.launch {
            foodRepository.addEntry(
                FoodEntry(
                    name = cleanName,
                    calories = calories.coerceAtLeast(0),
                    protein = protein.coerceAtLeast(0),
                    carbs = carbs.coerceAtLeast(0),
                    fat = fat.coerceAtLeast(0),
                    mealType = mealType,
                    epochDay = selectedDate.value.toEpochDay()
                )
            )
            widgetUpdater.refreshAll()
        }
    }

    fun deleteEntry(entry: FoodEntry) {
        viewModelScope.launch {
            foodRepository.deleteEntry(entry)
            widgetUpdater.refreshAll()
        }
    }

    fun setGoal(goal: Int) {
        viewModelScope.launch {
            settingsRepository.setDailyCalorieGoal(goal)
            widgetUpdater.refreshAll()
        }
    }

    fun goToToday() {
        selectedDate.value = LocalDate.now()
    }

    fun shiftDay(days: Long) {
        selectedDate.value = selectedDate.value.plusDays(days)
    }

    class Factory(
        private val foodRepository: FoodRepository,
        private val settingsRepository: SettingsRepository,
        private val widgetUpdater: WidgetUpdater
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            FoodViewModel(foodRepository, settingsRepository, widgetUpdater) as T
    }
}
