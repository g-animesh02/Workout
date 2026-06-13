package com.fittrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.seed.ExerciseLibrary
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import com.fittrack.app.ui.navigation.Routes
import com.fittrack.app.ui.navigation.TopLevelDestination
import com.fittrack.app.ui.screens.calendar.CalendarScreen
import com.fittrack.app.ui.screens.calendar.CalendarViewModel
import com.fittrack.app.ui.screens.exercise.ExerciseDetailScreen
import com.fittrack.app.ui.screens.food.FoodScreen
import com.fittrack.app.ui.screens.food.FoodViewModel
import com.fittrack.app.ui.screens.player.WorkoutPlayerScreen
import com.fittrack.app.ui.screens.schedule.ScheduleScreen
import com.fittrack.app.ui.screens.schedule.ScheduleUiState
import com.fittrack.app.ui.screens.schedule.ScheduleViewModel
import com.fittrack.app.ui.screens.workout.WorkoutDetailScreen
import com.fittrack.app.ui.theme.FitTrackTheme
import com.fittrack.app.widget.WidgetUpdater

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val startDestination = when (intent.getStringExtra(EXTRA_DESTINATION)) {
            DEST_FOOD -> Routes.FOOD
            else -> Routes.SCHEDULE
        }
        setContent {
            FitTrackTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FitTrackAppRoot(startDestination)
                }
            }
        }
    }

    companion object {
        const val EXTRA_DESTINATION = "extra_destination"
        const val DEST_SCHEDULE = "schedule"
        const val DEST_FOOD = "food"
    }
}

/** Resolves a workout id from the current program (covers custom-built workouts
 * that aren't in the static seed) or falls back to the seed catalog. */
private fun resolveWorkout(id: String?, state: ScheduleUiState): Workout? =
    state.selected.days.firstOrNull { it.workout?.id == id }?.workout
        ?: id?.let { WorkoutScheduleSeed.workoutById(it) }

@Composable
private fun FitTrackAppRoot(startDestination: String) {
    val navController = rememberNavController()
    val app = FitTrackApp.instance
    val foodViewModel: FoodViewModel = viewModel(
        factory = FoodViewModel.Factory(
            foodRepository = app.foodRepository,
            settingsRepository = app.settingsRepository,
            widgetUpdater = WidgetUpdater(app)
        )
    )
    val scheduleViewModel: ScheduleViewModel = viewModel(
        factory = ScheduleViewModel.Factory(
            settingsRepository = app.settingsRepository,
            widgetUpdater = WidgetUpdater(app)
        )
    )
    val calendarViewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModel.Factory(
            historyRepository = app.historyRepository,
            settingsRepository = app.settingsRepository
        )
    )
    val scheduleState by scheduleViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute == Routes.SCHEDULE ||
        currentRoute == Routes.FOOD || currentRoute == Routes.CALENDAR

    androidx.compose.material3.Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination
                    TopLevelDestination.entries.forEach { dest ->
                        val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    when (dest) {
                                        TopLevelDestination.SCHEDULE -> Icons.Filled.FitnessCenter
                                        TopLevelDestination.CALENDAR -> Icons.Filled.CalendarMonth
                                        TopLevelDestination.FOOD -> Icons.Filled.Restaurant
                                    },
                                    contentDescription = dest.label
                                )
                            },
                            label = { Text(dest.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.SCHEDULE) {
                ScheduleScreen(
                    viewModel = scheduleViewModel,
                    onWorkoutClick = { workoutId ->
                        navController.navigate(Routes.workoutDetail(workoutId))
                    }
                )
            }
            composable(Routes.FOOD) {
                FoodScreen(viewModel = foodViewModel)
            }
            composable(Routes.CALENDAR) {
                CalendarScreen(viewModel = calendarViewModel)
            }
            composable(Routes.WORKOUT_DETAIL) { entry ->
                val workoutId = entry.arguments?.getString(Routes.ARG_WORKOUT_ID)
                val workout = resolveWorkout(workoutId, scheduleState)
                if (workout != null) {
                    WorkoutDetailScreen(
                        workout = workout,
                        level = scheduleState.level,
                        onBack = { navController.popBackStack() },
                        onExerciseClick = { exerciseId ->
                            navController.navigate(Routes.exerciseDetail(exerciseId))
                        },
                        onStart = { navController.navigate(Routes.player(workout.id)) }
                    )
                }
            }
            composable(Routes.PLAYER) { entry ->
                val workoutId = entry.arguments?.getString(Routes.ARG_WORKOUT_ID)
                val workout = resolveWorkout(workoutId, scheduleState)
                if (workout != null) {
                    WorkoutPlayerScreen(
                        workout = workout,
                        level = scheduleState.level,
                        onExit = { navController.popBackStack() },
                        onCompleted = {
                            scope.launch { app.historyRepository.logWorkout(workout) }
                        }
                    )
                }
            }
            composable(Routes.EXERCISE_DETAIL) { entry ->
                val exerciseId = entry.arguments?.getString(Routes.ARG_EXERCISE_ID)
                val exercise = exerciseId?.let { runCatching { ExerciseLibrary.get(it) }.getOrNull() }
                if (exercise != null) {
                    ExerciseDetailScreen(
                        exercise = exercise,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
