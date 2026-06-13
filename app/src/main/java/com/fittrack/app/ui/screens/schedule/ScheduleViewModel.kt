package com.fittrack.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fittrack.app.data.model.WeeklyProgram
import com.fittrack.app.data.repository.SettingsRepository
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import com.fittrack.app.widget.WidgetUpdater
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScheduleUiState(
    val programs: List<WeeklyProgram> = WorkoutScheduleSeed.programs,
    val selected: WeeklyProgram = WorkoutScheduleSeed.balanced
)

/** Holds the user's chosen weekly program and exposes it to the schedule screen. */
class ScheduleViewModel(
    private val settingsRepository: SettingsRepository,
    private val widgetUpdater: WidgetUpdater
) : ViewModel() {

    val uiState: StateFlow<ScheduleUiState> =
        settingsRepository.selectedProgramId
            .map { id ->
                ScheduleUiState(
                    programs = WorkoutScheduleSeed.programs,
                    selected = WorkoutScheduleSeed.programById(id)
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScheduleUiState())

    fun selectProgram(id: String) {
        viewModelScope.launch {
            settingsRepository.setSelectedProgram(id)
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
