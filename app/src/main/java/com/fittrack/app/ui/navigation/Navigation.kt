package com.fittrack.app.ui.navigation

/** Type-safe route definitions for the app's navigation graph. */
object Routes {
    const val SCHEDULE = "schedule"
    const val FOOD = "food"
    const val CALENDAR = "calendar"

    const val WORKOUT_DETAIL = "workout/{workoutId}"
    fun workoutDetail(workoutId: String) = "workout/$workoutId"

    const val EXERCISE_DETAIL = "exercise/{exerciseId}"
    fun exerciseDetail(exerciseId: String) = "exercise/$exerciseId"

    const val PLAYER = "player/{workoutId}"
    fun player(workoutId: String) = "player/$workoutId"

    const val ARG_WORKOUT_ID = "workoutId"
    const val ARG_EXERCISE_ID = "exerciseId"
}

enum class TopLevelDestination(val route: String, val label: String) {
    SCHEDULE(Routes.SCHEDULE, "Plan"),
    CALENDAR(Routes.CALENDAR, "History"),
    FOOD(Routes.FOOD, "Food")
}
