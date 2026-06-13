package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.Exercise
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
 * exercise, more reps, and more exercises so the whole session fills a target
 * duration: ~20 min (Beginner), ~35 min (Intermediate), ~50 min (Advanced).
 */
object LevelScaling {

    /** Target total session length in minutes for a level. */
    fun targetMinutes(level: Difficulty): Int = when (level) {
        Difficulty.BEGINNER -> 20
        Difficulty.INTERMEDIATE -> 35
        Difficulty.ADVANCED -> 50
    }

    fun workSeconds(base: Int?, level: Difficulty): Int {
        val b = base ?: 40
        val factor = when (level) {
            Difficulty.BEGINNER -> 0.8
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
            Difficulty.ADVANCED -> 1.4
        }
        val target = max(5, (base * factor).roundToInt())
        return if (reps.contains("per", ignoreCase = true)) "$target reps per side" else "$target reps"
    }

    /** Short description of how this level changes the workout. */
    fun summary(level: Difficulty): String = when (level) {
        Difficulty.BEGINNER -> "~20 min · lighter pace, fewer reps"
        Difficulty.INTERMEDIATE -> "~35 min · standard pace & reps"
        Difficulty.ADVANCED -> "~50 min · longer, more reps & exercises"
    }

    /**
     * Build the level-scaled session: cycles through the (scaled) exercises until
     * the target duration is reached, so the number of exercises and total time
     * both grow with the level.
     */
    fun plan(exercises: List<Exercise>, level: Difficulty): List<WorkoutStep> {
        if (exercises.isEmpty()) return emptyList()
        val scaled = exercises.map { ex ->
            WorkoutStep(
                exercise = ex,
                workSeconds = workSeconds(ex.workSeconds, level),
                reps = reps(ex.reps, level),
                restSeconds = restSeconds(ex.restSeconds, level)
            )
        }
        val targetSeconds = targetMinutes(level) * 60
        val result = ArrayList<WorkoutStep>()
        var total = 0
        var i = 0
        while (total < targetSeconds && result.size < 80) {
            val step = scaled[i % scaled.size]
            result.add(step)
            total += step.workSeconds + step.restSeconds
            i++
        }
        return result
    }

    /** Total minutes a plan will take. */
    fun planMinutes(steps: List<WorkoutStep>): Int =
        max(1, (steps.sumOf { it.workSeconds + it.restSeconds } / 60.0).roundToInt())
}
