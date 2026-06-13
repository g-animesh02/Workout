package com.fittrack.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.WeeklyProgram
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.model.WorkoutDay
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.data.repository.SettingsRepository
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import com.fittrack.app.widget.WidgetUpdater
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek

data class ProgramChip(val id: String, val name: String, val accent: WorkoutType)

data class ScheduleUiState(
    val chips: List<ProgramChip> = emptyList(),
    val selectedId: String = WorkoutScheduleSeed.DEFAULT_PROGRAM_ID,
    val selected: WeeklyProgram = WorkoutScheduleSeed.balanced,
    val level: Difficulty = Difficulty.INTERMEDIATE,
    val rounds: Int = 3,
    val allWorkouts: List<Workout> = WorkoutScheduleSeed.allWorkouts
) {
    val isCustom: Boolean get() = selectedId == WorkoutScheduleSeed.CUSTOM_PROGRAM_ID
}

/** Holds the chosen program, difficulty level and custom schedule. */
class ScheduleViewModel(
    private val settingsRepository: SettingsRepository,
    private val widgetUpdater: WidgetUpdater
) : ViewModel() {

    private val chips: List<ProgramChip> =
        WorkoutScheduleSeed.programs.map { ProgramChip(it.id, it.name, it.accent) } +
            ProgramChip(WorkoutScheduleSeed.CUSTOM_PROGRAM_ID, "Custom", WorkoutType.FULL_BODY)

    val uiState: StateFlow<ScheduleUiState> =
        combine(
            settingsRepository.selectedProgramId,
            settingsRepository.selectedLevel,
            settingsRepository.customSchedule
        ) { id, level, custom ->
            val selected =
                if (id == WorkoutScheduleSeed.CUSTOM_PROGRAM_ID) buildCustomProgram(custom)
                else WorkoutScheduleSeed.programById(id)
            ScheduleUiState(
                chips = chips,
                selectedId = if (id == WorkoutScheduleSeed.CUSTOM_PROGRAM_ID) id else selected.id,
                selected = selected,
                level = level,
                rounds = WorkoutScheduleSeed.roundsForLevel(level),
                allWorkouts = WorkoutScheduleSeed.allWorkouts
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScheduleUiState(chips = chips))

    private fun buildCustomProgram(map: Map<DayOfWeek, String?>): WeeklyProgram {
        val days = DayOfWeek.entries.map { day ->
            val workout = map[day]?.let { WorkoutScheduleSeed.workoutById(it) }
            WorkoutDay(day, workout?.name ?: "Rest Day", workout)
        }
        return WeeklyProgram(
            id = WorkoutScheduleSeed.CUSTOM_PROGRAM_ID,
            name = "Custom",
            tagline = "Your own plan",
            description = "Tap any day to choose a workout or set it as a rest day.",
            accent = WorkoutType.FULL_BODY,
            days = days
        )
    }

    fun selectProgram(id: String) {
        viewModelScope.launch {
            settingsRepository.setSelectedProgram(id)
            widgetUpdater.refreshAll()
        }
    }

    fun setLevel(level: Difficulty) {
        viewModelScope.launch { settingsRepository.setLevel(level) }
    }

    fun setCustomDay(day: DayOfWeek, workoutId: String?) {
        viewModelScope.launch {
            settingsRepository.setCustomDay(day, workoutId)
            widgetUpdater.refreshAll()
        }
    }

    class Factory(
        private val settingsRepository: SettingsRepository,
        private val widgetUpdater: WidgetUpdater
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ScheduleViewModel(settingsRepository, widgetUpdater) as T
    }
}
