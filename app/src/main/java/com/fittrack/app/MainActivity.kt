package com.fittrack.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fittrack.app.data.seed.ExerciseLibrary
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import com.fittrack.app.ui.navigation.Routes
import com.fittrack.app.ui.navigation.TopLevelDestination
import com.fittrack.app.ui.screens.exercise.ExerciseDetailScreen
import com.fittrack.app.ui.screens.food.FoodScreen
import com.fittrack.app.ui.screens.food.FoodViewModel
import com.fittrack.app.ui.screens.schedule.ScheduleScreen
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute == Routes.SCHEDULE || currentRoute == Routes.FOOD

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
                                        TopLevelDestination.SCHEDULE -> Icons.Filled.CalendarMonth
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
            composable(Routes.WORKOUT_DETAIL) { entry ->
                val workoutId = entry.arguments?.getString(Routes.ARG_WORKOUT_ID)
                val workout = workoutId?.let { WorkoutScheduleSeed.workoutById(it) }
                if (workout != null) {
                    WorkoutDetailScreen(
                        workout = workout,
                        onBack = { navController.popBackStack() },
                        onExerciseClick = { exerciseId ->
                            navController.navigate(Routes.exerciseDetail(exerciseId))
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
