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
import com.fittrack.app.data.seed.ExerciseLibrary
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

/** Holds the chosen program, difficulty level, custom schedule and custom-built workouts. */
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
            settingsRepository.customSchedule,
            settingsRepository.customDayWorkouts
        ) { id, level, custom, customWorkouts ->
            val selected =
                if (id == WorkoutScheduleSeed.CUSTOM_PROGRAM_ID) buildCustomProgram(custom, customWorkouts)
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

    private fun buildCustomProgram(
        scheduleMap: Map<DayOfWeek, String?>,
        workoutsMap: Map<DayOfWeek, List<String>>
    ): WeeklyProgram {
        val days = DayOfWeek.entries.map { day ->
            val builtIds = workoutsMap[day]
            val workout = when {
                !builtIds.isNullOrEmpty() -> buildCustomWorkout(day, builtIds)
                else -> scheduleMap[day]?.let { WorkoutScheduleSeed.workoutById(it) }
            }
            WorkoutDay(day, workout?.name ?: "Rest Day", workout)
        }
        return WeeklyProgram(
            id = WorkoutScheduleSeed.CUSTOM_PROGRAM_ID,
            name = "Custom",
            tagline = "Your own plan",
            description = "Tap any day to choose a workout, build your own mix, or set a rest day.",
            accent = WorkoutType.FULL_BODY,
            days = days
        )
    }

    private fun buildCustomWorkout(day: DayOfWeek, ids: List<String>): Workout {
        val exercises = ids.mapNotNull { runCatching { ExerciseLibrary.get(it) }.getOrNull() }
        return Workout(
            id = "custom_${day.name}",
            name = "Custom Mix",
            type = WorkoutType.FULL_BODY,
            description = "Your custom-built workout. Move through each exercise and repeat for your " +
                "suggested number of rounds.",
            focus = exercises.map { it.category.label }.distinct().joinToString(" · "),
            difficulty = Difficulty.INTERMEDIATE,
            estimatedMinutes = (exercises.size * 4).coerceAtLeast(10),
            exercises = exercises
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
            // Picking a preset (or rest) clears any built-your-own workout for the day.
            settingsRepository.setCustomDayExercises(day, emptyList())
            settingsRepository.setCustomDay(day, workoutId)
            widgetUpdater.refreshAll()
        }
    }

    fun setCustomDayExercises(day: DayOfWeek, exerciseIds: List<String>) {
        viewModelScope.launch {
            settingsRepository.setCustomDayExercises(day, exerciseIds)
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
