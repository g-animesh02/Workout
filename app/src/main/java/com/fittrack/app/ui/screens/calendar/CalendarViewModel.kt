package com.fittrack.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fittrack.app.data.db.entity.WorkoutLog
import com.fittrack.app.data.repository.HistoryRepository
import com.fittrack.app.data.repository.SettingsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.YearMonth

data class DayStat(val calories: Int, val workouts: Int)

data class CalendarUiState(
    val month: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val stats: Map<LocalDate, DayStat> = emptyMap(),
    val selectedWorkouts: List<WorkoutLog> = emptyList(),
    val calorieGoal: Int = SettingsRepository.DEFAULT_CALORIE_GOAL
) {
    val selectedCalories: Int get() = stats[selectedDate]?.calories ?: 0
}

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val month = MutableStateFlow(YearMonth.now())
    private val selectedDate = MutableStateFlow(LocalDate.now())

    private val monthStats = month.flatMapLatest { ym ->
        val start = ym.atDay(1)
        val end = ym.atEndOfMonth()
        combine(
            historyRepository.dailyCalories(start, end),
            historyRepository.dailyWorkoutCounts(start, end)
        ) { calories, counts ->
            val map = HashMap<LocalDate, DayStat>()
            calories.forEach { map[LocalDate.ofEpochDay(it.epochDay)] = DayStat(it.calories, 0) }
            counts.forEach {
                val date = LocalDate.ofEpochDay(it.epochDay)
                val existing = map[date]
                map[date] = DayStat(existing?.calories ?: 0, it.count)
            }
            ym to map
        }
    }

    private val selectedLogs = selectedDate.flatMapLatest { historyRepository.logsForDay(it) }

    val uiState: StateFlow<CalendarUiState> =
        combine(
            monthStats,
            selectedDate,
            selectedLogs,
            settingsRepository.dailyCalorieGoal
        ) { (ym, stats), date, logs, goal ->
            CalendarUiState(
                month = ym,
                selectedDate = date,
                stats = stats,
                selectedWorkouts = logs,
                calorieGoal = goal
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CalendarUiState())

    fun selectDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun shiftMonth(delta: Long) {
        val newMonth = month.value.plusMonths(delta)
        month.value = newMonth
        val today = LocalDate.now()
        selectedDate.value = if (YearMonth.from(today) == newMonth) today else newMonth.atDay(1)
    }

    class Factory(
        private val historyRepository: HistoryRepository,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CalendarViewModel(historyRepository, settingsRepository) as T
    }
}
