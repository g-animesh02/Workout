package com.fittrack.app

import com.fittrack.app.data.seed.ExerciseLibrary
import com.fittrack.app.data.seed.WorkoutScheduleSeed
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek

/** Pure-JVM checks that the bundled training content is well-formed. */
class WorkoutDataTest {

    @Test
    fun weeklyPlanCoversEveryDay() {
        val days = WorkoutScheduleSeed.weeklyPlan.map { it.dayOfWeek }.toSet()
        assertEquals(DayOfWeek.entries.toSet(), days)
    }

    @Test
    fun everyWorkoutHasExercises() {
        WorkoutScheduleSeed.allWorkouts.forEach { workout ->
            assertTrue("${workout.name} must have exercises", workout.exercises.isNotEmpty())
        }
    }

    @Test
    fun exerciseIdsAreUnique() {
        val ids = ExerciseLibrary.all.map { it.id }
        assertEquals("Exercise ids must be unique", ids.size, ids.toSet().size)
    }

    @Test
    fun everyExerciseHasInstructions() {
        ExerciseLibrary.all.forEach { exercise ->
            assertTrue("${exercise.name} must explain how to do it", exercise.instructions.isNotEmpty())
            assertTrue("${exercise.name} must describe what it is", exercise.description.isNotBlank())
        }
    }

    @Test
    fun scheduledWorkoutsResolveById() {
        WorkoutScheduleSeed.weeklyPlan.mapNotNull { it.workout }.forEach { workout ->
            assertEquals(workout, WorkoutScheduleSeed.workoutById(workout.id))
        }
    }
}
