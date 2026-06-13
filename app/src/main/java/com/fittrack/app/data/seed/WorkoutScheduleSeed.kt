package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.model.WorkoutDay
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.data.seed.ExerciseLibrary as Ex
import java.time.DayOfWeek

/**
 * The default, ready-to-use weekly training plan. It balances HIIT, strength,
 * core and recovery across the week so a beginner can start immediately with
 * zero setup and no cost.
 */
object WorkoutScheduleSeed {

    val hiitBlast = Workout(
        id = "hiit_blast",
        name = "HIIT Blast",
        type = WorkoutType.HIIT,
        description = "A fast, high-intensity interval session. Work hard during each interval, then " +
            "recover briefly before the next. Repeat the circuit 3 rounds for a full session.",
        focus = "Fat burn · Conditioning · Full body",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 25,
        exercises = listOf(
            Ex.jumpingJacks, Ex.highKnees, Ex.burpees, Ex.mountainClimbers,
            Ex.jumpSquats, Ex.skaters, Ex.plankJacks
        )
    )

    val upperBody = Workout(
        id = "upper_body_strength",
        name = "Upper Body Strength",
        type = WorkoutType.STRENGTH,
        description = "Build pushing and pressing strength in the chest, shoulders and arms using only " +
            "your bodyweight. Complete 3 sets of each exercise.",
        focus = "Chest · Shoulders · Arms",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 30,
        exercises = listOf(
            Ex.pushUp, Ex.pikePushUp, Ex.tricepDip, Ex.supermanRaise, Ex.plank
        )
    )

    val lowerBody = Workout(
        id = "lower_body_strength",
        name = "Lower Body Strength",
        type = WorkoutType.STRENGTH,
        description = "Strengthen your legs and glutes with foundational lower-body movements. " +
            "Complete 3 sets of each exercise, resting as needed.",
        focus = "Quads · Glutes · Hamstrings",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 30,
        exercises = listOf(
            Ex.squat, Ex.reverseLunge, Ex.gluteBridge, Ex.singleLegDeadlift,
            Ex.wallSit, Ex.calfRaise
        )
    )

    val coreCrusher = Workout(
        id = "core_crusher",
        name = "Core & Abs",
        type = WorkoutType.CORE,
        description = "A focused core circuit to build a strong, stable midsection. Move through each " +
            "exercise with control and repeat for 2–3 rounds.",
        focus = "Abs · Obliques · Core stability",
        difficulty = Difficulty.BEGINNER,
        estimatedMinutes = 20,
        exercises = listOf(
            Ex.plank, Ex.bicycleCrunch, Ex.deadBug, Ex.legRaise,
            Ex.russianTwist, Ex.hollowHold, Ex.flutterKicks, Ex.sidePlank
        )
    )

    val fullBodyHiit = Workout(
        id = "full_body_hiit",
        name = "Full Body Burn",
        type = WorkoutType.FULL_BODY,
        description = "An end-of-week full-body finisher mixing strength and HIIT. Push through 3 rounds " +
            "to cap off your training week.",
        focus = "Total body · Strength + cardio",
        difficulty = Difficulty.ADVANCED,
        estimatedMinutes = 30,
        exercises = listOf(
            Ex.burpees, Ex.squat, Ex.pushUp, Ex.mountainClimbers,
            Ex.reverseLunge, Ex.plankJacks, Ex.jumpSquats
        )
    )

    val activeRecovery = Workout(
        id = "active_recovery",
        name = "Mobility & Stretch",
        type = WorkoutType.FLEXIBILITY,
        description = "A gentle recovery flow to loosen tight muscles, improve mobility and help you " +
            "recover for the week ahead. Hold each position calmly and breathe.",
        focus = "Mobility · Flexibility · Recovery",
        difficulty = Difficulty.BEGINNER,
        estimatedMinutes = 15,
        exercises = listOf(
            Ex.marchInPlace, Ex.catCow, Ex.childPose, Ex.downwardDog,
            Ex.worldsGreatest, Ex.hamstringStretch, Ex.chestOpener
        )
    )

    /** All distinct workouts, useful for a "browse workouts" library screen. */
    val allWorkouts: List<Workout> = listOf(
        hiitBlast, upperBody, lowerBody, coreCrusher, fullBodyHiit, activeRecovery
    )

    fun workoutById(id: String): Workout? = allWorkouts.firstOrNull { it.id == id }

    /** The default Monday–Sunday plan. */
    val weeklyPlan: List<WorkoutDay> = listOf(
        WorkoutDay(DayOfWeek.MONDAY, "HIIT Blast", hiitBlast),
        WorkoutDay(DayOfWeek.TUESDAY, "Upper Body Strength", upperBody),
        WorkoutDay(DayOfWeek.WEDNESDAY, "Lower Body Strength", lowerBody),
        WorkoutDay(DayOfWeek.THURSDAY, "Core & Abs", coreCrusher),
        WorkoutDay(DayOfWeek.FRIDAY, "Full Body Burn", fullBodyHiit),
        WorkoutDay(DayOfWeek.SATURDAY, "Mobility & Stretch", activeRecovery),
        WorkoutDay(DayOfWeek.SUNDAY, "Rest Day", null)
    )

    fun dayFor(dayOfWeek: DayOfWeek): WorkoutDay =
        weeklyPlan.first { it.dayOfWeek == dayOfWeek }
}
