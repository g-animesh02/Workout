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

    @Test
    fun everyProgramCoversEveryDayAndHasTraining() {
        assertTrue("There should be multiple programs", WorkoutScheduleSeed.programs.size >= 2)
        WorkoutScheduleSeed.programs.forEach { program ->
            val days = program.days.map { it.dayOfWeek }.toSet()
            assertEquals("${program.name} must cover all 7 days", DayOfWeek.entries.toSet(), days)
            assertTrue("${program.name} must have at least one training day", program.trainingDays > 0)
        }
    }

    @Test
    fun everyProgramWorkoutResolvesAndHasContent() {
        WorkoutScheduleSeed.programs.flatMap { it.days }.mapNotNull { it.workout }.forEach { workout ->
            assertEquals(workout, WorkoutScheduleSeed.workoutById(workout.id))
            assertTrue("${workout.name} must have exercises", workout.exercises.isNotEmpty())
        }
    }

    @Test
    fun defaultProgramIdResolves() {
        val program = WorkoutScheduleSeed.programById(WorkoutScheduleSeed.DEFAULT_PROGRAM_ID)
        assertEquals(WorkoutScheduleSeed.DEFAULT_PROGRAM_ID, program.id)
    }

    @Test
    fun higherLevelMeansLongerWorkAndMoreExercises() {
        val ex = WorkoutScheduleSeed.hiitBlast.exercises
        val beginner = com.fittrack.app.data.seed.LevelScaling.plan(ex, com.fittrack.app.data.model.Difficulty.BEGINNER)
        val advanced = com.fittrack.app.data.seed.LevelScaling.plan(ex, com.fittrack.app.data.model.Difficulty.ADVANCED)
        assertTrue("Advanced should have at least as many exercises", advanced.size >= beginner.size)
        val baseWork = com.fittrack.app.data.seed.LevelScaling.workSeconds(40, com.fittrack.app.data.model.Difficulty.BEGINNER)
        val advWork = com.fittrack.app.data.seed.LevelScaling.workSeconds(40, com.fittrack.app.data.model.Difficulty.ADVANCED)
        assertTrue("Advanced work time should exceed beginner", advWork > baseWork)
    }

    @Test
    fun mixedWorkoutDrawsFromManyCategories() {
        val categories = WorkoutScheduleSeed.mixedCircuit.exercises.map { it.category }.toSet()
        assertTrue("Mixed circuit should span several categories", categories.size >= 4)
    }
}
