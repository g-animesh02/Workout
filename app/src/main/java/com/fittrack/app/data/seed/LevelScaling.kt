package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.Exercise
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/** One step in a guided session: an exercise with its (level-scaled) work time,
 * reps target and rest. Editable live during execution. */
data class WorkoutStep(
    val exercise: Exercise,
    val workSeconds: Int,
    val reps: String?,
    val restSeconds: Int
)

/**
 * Scales a workout by difficulty level. Higher levels mean longer work time per
 * exercise, more reps, and more exercises in the session (rather than simply
 * repeating the circuit for more rounds).
 */
object LevelScaling {

    fun workSeconds(base: Int?, level: Difficulty): Int {
        val b = base ?: 40
        val factor = when (level) {
            Difficulty.BEGINNER -> 0.75
            Difficulty.INTERMEDIATE -> 1.0
            Difficulty.ADVANCED -> 1.3
        }
        return ((b * factor / 5.0).roundToInt() * 5).coerceIn(15, 120)
    }

    fun restSeconds(base: Int?, level: Difficulty): Int {
        val b = base ?: 20
        return when (level) {
            Difficulty.BEGINNER -> b + 10
            Difficulty.INTERMEDIATE -> b
            Difficulty.ADVANCED -> max(10, b - 5)
        }
    }

    fun reps(reps: String?, level: Difficulty): String? {
        if (reps == null) return null
        val base = Regex("\\d+").findAll(reps).map { it.value.toInt() }.toList().maxOrNull() ?: return reps
        val factor = when (level) {
            Difficulty.BEGINNER -> 0.7
            Difficulty.INTERMEDIATE -> 1.0
            Difficulty.ADVANCED -> 1.3
        }
        val target = max(5, (base * factor).roundToInt())
        return if (reps.contains("per", ignoreCase = true)) "$target reps per side" else "$target reps"
    }

    /** Short description of how this level changes the workout. */
    fun summary(level: Difficulty): String = when (level) {
        Difficulty.BEGINNER -> "shorter time · fewer reps & exercises"
        Difficulty.INTERMEDIATE -> "standard time, reps & exercises"
        Difficulty.ADVANCED -> "longer time · more reps & exercises"
    }

    /** Build the ordered, level-scaled session steps for a workout. */
    fun plan(exercises: List<Exercise>, level: Difficulty): List<WorkoutStep> {
        if (exercises.isEmpty()) return emptyList()
        val selected: List<Exercise> = when (level) {
            Difficulty.BEGINNER -> exercises.take(max(3, ceil(exercises.size * 0.7).toInt()))
            Difficulty.INTERMEDIATE -> exercises
            Difficulty.ADVANCED -> exercises + exercises.take(max(2, ceil(exercises.size * 0.5).toInt()))
        }
        return selected.map { ex ->
            WorkoutStep(
                exercise = ex,
                workSeconds = workSeconds(ex.workSeconds, level),
                reps = reps(ex.reps, level),
                restSeconds = restSeconds(ex.restSeconds, level)
            )
        }
    }
}
