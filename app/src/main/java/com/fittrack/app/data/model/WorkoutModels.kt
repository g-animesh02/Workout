package com.fittrack.app.data.model

/** Broad category of a workout, used for theming and filtering. */
enum class WorkoutType(val label: String) {
    HIIT("HIIT"),
    STRENGTH("Strength"),
    CARDIO("Cardio"),
    CORE("Core"),
    FLEXIBILITY("Mobility"),
    FULL_BODY("Full Body"),
    REST("Rest")
}

enum class Difficulty(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

/**
 * A single exercise with a full explanation of what it is and how to perform it.
 * [instructions] is an ordered list of step-by-step cues.
 */
data class Exercise(
    val id: String,
    val name: String,
    val category: WorkoutType,
    val description: String,
    val instructions: List<String>,
    val targetMuscles: List<String>,
    val equipment: String = "None (bodyweight)",
    val sets: Int? = null,
    val reps: String? = null,
    val workSeconds: Int? = null,
    val restSeconds: Int? = null,
    val tips: List<String> = emptyList()
) {
    /** Short human-readable dosage line, e.g. "3 sets x 12 reps" or "40s work / 20s rest". */
    val dosage: String
        get() = when {
            workSeconds != null && restSeconds != null -> "${workSeconds}s work · ${restSeconds}s rest"
            sets != null && reps != null -> "$sets sets × $reps"
            reps != null -> reps
            workSeconds != null -> "${workSeconds}s"
            else -> ""
        }
}

/** A complete workout session made up of several exercises. */
data class Workout(
    val id: String,
    val name: String,
    val type: WorkoutType,
    val description: String,
    val focus: String,
    val difficulty: Difficulty,
    val estimatedMinutes: Int,
    val exercises: List<Exercise>
)

/** One day in the default weekly schedule. A rest day has a null [workout]. */
data class WorkoutDay(
    val dayOfWeek: java.time.DayOfWeek,
    val title: String,
    val workout: Workout?
) {
    val isRest: Boolean get() = workout == null
}
