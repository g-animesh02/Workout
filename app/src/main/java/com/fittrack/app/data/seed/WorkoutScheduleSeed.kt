package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Difficulty
import com.fittrack.app.data.model.WeeklyProgram
import com.fittrack.app.data.model.Workout
import com.fittrack.app.data.model.WorkoutDay
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.data.seed.ExerciseLibrary as Ex
import com.fittrack.app.data.seed.GymExerciseLibrary as Gym
import java.time.DayOfWeek

/**
 * Bundled workouts and the ready-made weekly programs that combine them.
 * The user can pick a program (Balanced, HIIT, Strength split, or a
 * single-muscle gym split) and follow its full Monday–Sunday schedule.
 * Everything is offline and free.
 */
object WorkoutScheduleSeed {

    // =====================================================================
    // Bodyweight / mixed workouts
    // =====================================================================

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

    val cardioBurner = Workout(
        id = "cardio_burner",
        name = "Cardio Burner",
        type = WorkoutType.CARDIO,
        description = "A pure cardio interval circuit to torch calories and build endurance. Keep moving " +
            "through each interval and repeat for 3–4 rounds.",
        focus = "Endurance · Calorie burn",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 22,
        exercises = listOf(
            Ex.jumpingJacks, Ex.highKnees, Ex.buttKicks, Ex.skaters,
            Ex.mountainClimbers, Ex.jumpSquats
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

    // =====================================================================
    // Mixed workouts — 2–3 moves from every category
    // =====================================================================

    val mixedCircuit = Workout(
        id = "mixed_circuit",
        name = "Mixed Circuit",
        type = WorkoutType.FULL_BODY,
        description = "A little of everything: 2–3 moves each from HIIT, strength, core, cardio and " +
            "mobility. A balanced, varied session that hits the whole body. Repeat for 2–3 rounds.",
        focus = "HIIT · Strength · Core · Cardio · Mobility",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 28,
        exercises = listOf(
            // HIIT
            Ex.jumpingJacks, Ex.burpees,
            // Strength
            Ex.pushUp, Ex.squat,
            // Core
            Ex.plank, Ex.bicycleCrunch,
            // Cardio
            Ex.highKnees, Ex.mountainClimbers,
            // Mobility
            Ex.downwardDog, Ex.worldsGreatest
        )
    )

    val mixedExpress = Workout(
        id = "mixed_express",
        name = "Mixed Express",
        type = WorkoutType.FULL_BODY,
        description = "A shorter mixed session pulling one or two moves from each style for a quick, " +
            "well-rounded workout when you're short on time.",
        focus = "Quick · Full body · Varied",
        difficulty = Difficulty.BEGINNER,
        estimatedMinutes = 18,
        exercises = listOf(
            Ex.jumpingJacks, Ex.squat, Ex.pushUp, Ex.plank, Ex.skaters, Ex.catCow
        )
    )

    // =====================================================================
    // Gym strength split workouts (Push / Pull / Legs)
    // =====================================================================

    val pushDay = Workout(
        id = "push_day",
        name = "Push Day",
        type = WorkoutType.STRENGTH,
        description = "All the pushing muscles in one session — chest, shoulders and triceps. Rest fully " +
            "between heavy sets and focus on good form.",
        focus = "Chest · Shoulders · Triceps",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 50,
        exercises = listOf(
            Gym.benchPress, Gym.overheadPress, Gym.inclineDbPress,
            Gym.lateralRaise, Gym.tricepPushdown
        )
    )

    val pullDay = Workout(
        id = "pull_day",
        name = "Pull Day",
        type = WorkoutType.STRENGTH,
        description = "All the pulling muscles — back and biceps. Build a strong, wide back and bigger arms.",
        focus = "Back · Lats · Biceps",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 50,
        exercises = listOf(
            Gym.pullUp, Gym.bentOverRow, Gym.latPulldown, Gym.seatedRow, Gym.barbellCurl
        )
    )

    val legDayGym = Workout(
        id = "leg_day_gym",
        name = "Leg Day",
        type = WorkoutType.STRENGTH,
        description = "A complete lower-body gym session built around the squat and hip hinge. Don't skip it!",
        focus = "Quads · Glutes · Hamstrings · Calves",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 55,
        exercises = listOf(
            Gym.barbellSquat, Gym.romanianDeadlift, Gym.legPress,
            Gym.legCurl, Gym.legExtension, Ex.calfRaise
        )
    )

    // =====================================================================
    // Single-muscle gym split workouts (Bro split)
    // =====================================================================

    val chestDay = Workout(
        id = "chest_day",
        name = "Chest Day",
        type = WorkoutType.STRENGTH,
        description = "A dedicated chest session hitting every angle, from flat pressing to flyes. " +
            "Complete all sets with controlled form.",
        focus = "Chest (+ triceps)",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 45,
        exercises = listOf(
            Gym.benchPress, Gym.inclineDbPress, Gym.chestFly, Ex.pushUp, Ex.tricepDip
        )
    )

    val backDay = Workout(
        id = "back_day",
        name = "Back Day",
        type = WorkoutType.STRENGTH,
        description = "Build a thick, wide back with vertical and horizontal pulls plus the deadlift.",
        focus = "Lats · Mid back · Lower back",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 50,
        exercises = listOf(
            Gym.pullUp, Gym.bentOverRow, Gym.latPulldown, Gym.seatedRow, Gym.deadlift
        )
    )

    val shoulderDay = Workout(
        id = "shoulder_day",
        name = "Shoulder Day",
        type = WorkoutType.STRENGTH,
        description = "Round, capped shoulders from all three angles — front, side and rear delts — plus traps.",
        focus = "Shoulders · Traps",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 45,
        exercises = listOf(
            Gym.overheadPress, Gym.lateralRaise, Gym.frontRaise, Gym.rearDeltFly, Gym.shrug
        )
    )

    val armDay = Workout(
        id = "arm_day",
        name = "Arm Day",
        type = WorkoutType.STRENGTH,
        description = "Biceps and triceps supersets for bigger, stronger arms. Pair a biceps and triceps " +
            "move back to back to save time.",
        focus = "Biceps · Triceps",
        difficulty = Difficulty.INTERMEDIATE,
        estimatedMinutes = 40,
        exercises = listOf(
            Gym.barbellCurl, Gym.hammerCurl, Gym.tricepPushdown, Gym.overheadTricepExt, Ex.tricepDip
        )
    )

    // =====================================================================
    // Lookups
    // =====================================================================

    /** Every distinct workout used by any program (for navigation/detail lookup). */
    val allWorkouts: List<Workout> by lazy {
        programs.flatMap { it.days }.mapNotNull { it.workout }.distinctBy { it.id }
    }

    fun workoutById(id: String): Workout? = allWorkouts.firstOrNull { it.id == id }

    // =====================================================================
    // Weekly programs
    // =====================================================================

    private fun rest(day: DayOfWeek) = WorkoutDay(day, "Rest Day", null)

    val balanced = WeeklyProgram(
        id = "balanced",
        name = "Balanced",
        tagline = "A bit of everything",
        description = "A well-rounded week of HIIT, strength, core and recovery. Perfect if you want " +
            "general fitness without specialising.",
        accent = WorkoutType.FULL_BODY,
        days = listOf(
            WorkoutDay(DayOfWeek.MONDAY, "HIIT Blast", hiitBlast),
            WorkoutDay(DayOfWeek.TUESDAY, "Upper Body Strength", upperBody),
            WorkoutDay(DayOfWeek.WEDNESDAY, "Lower Body Strength", lowerBody),
            WorkoutDay(DayOfWeek.THURSDAY, "Core & Abs", coreCrusher),
            WorkoutDay(DayOfWeek.FRIDAY, "Full Body Burn", fullBodyHiit),
            WorkoutDay(DayOfWeek.SATURDAY, "Mobility & Stretch", activeRecovery),
            rest(DayOfWeek.SUNDAY)
        )
    )

    val hiitProgram = WeeklyProgram(
        id = "hiit",
        name = "HIIT Shred",
        tagline = "High-intensity fat burn",
        description = "A conditioning-focused week of high-intensity intervals and cardio, with core work " +
            "and a recovery day to keep you fresh. Great for burning calories with no equipment.",
        accent = WorkoutType.HIIT,
        days = listOf(
            WorkoutDay(DayOfWeek.MONDAY, "HIIT Blast", hiitBlast),
            WorkoutDay(DayOfWeek.TUESDAY, "Core & Abs", coreCrusher),
            WorkoutDay(DayOfWeek.WEDNESDAY, "Cardio Burner", cardioBurner),
            WorkoutDay(DayOfWeek.THURSDAY, "Mobility & Stretch", activeRecovery),
            WorkoutDay(DayOfWeek.FRIDAY, "HIIT Blast", hiitBlast),
            WorkoutDay(DayOfWeek.SATURDAY, "Full Body Burn", fullBodyHiit),
            rest(DayOfWeek.SUNDAY)
        )
    )

    val strengthPPL = WeeklyProgram(
        id = "strength_ppl",
        name = "Strength (PPL)",
        tagline = "Push · Pull · Legs",
        description = "A classic gym strength split. Train pushing muscles, pulling muscles and legs on " +
            "separate days for balanced, progressive strength. Requires basic gym equipment.",
        accent = WorkoutType.STRENGTH,
        days = listOf(
            WorkoutDay(DayOfWeek.MONDAY, "Push Day", pushDay),
            WorkoutDay(DayOfWeek.TUESDAY, "Pull Day", pullDay),
            WorkoutDay(DayOfWeek.WEDNESDAY, "Leg Day", legDayGym),
            rest(DayOfWeek.THURSDAY),
            WorkoutDay(DayOfWeek.FRIDAY, "Push Day", pushDay),
            WorkoutDay(DayOfWeek.SATURDAY, "Pull Day", pullDay),
            rest(DayOfWeek.SUNDAY)
        )
    )

    val broSplit = WeeklyProgram(
        id = "bro_split",
        name = "Gym Muscle Split",
        tagline = "One muscle group per day",
        description = "A single-muscle 'bro split': hit one body part per day for maximum focus and volume. " +
            "Chest, back, shoulders, arms, legs, then core. Requires gym equipment.",
        accent = WorkoutType.STRENGTH,
        days = listOf(
            WorkoutDay(DayOfWeek.MONDAY, "Chest Day", chestDay),
            WorkoutDay(DayOfWeek.TUESDAY, "Back Day", backDay),
            WorkoutDay(DayOfWeek.WEDNESDAY, "Shoulder Day", shoulderDay),
            WorkoutDay(DayOfWeek.THURSDAY, "Arm Day", armDay),
            WorkoutDay(DayOfWeek.FRIDAY, "Leg Day", legDayGym),
            WorkoutDay(DayOfWeek.SATURDAY, "Core & Abs", coreCrusher),
            rest(DayOfWeek.SUNDAY)
        )
    )

    val mixedProgram = WeeklyProgram(
        id = "mixed",
        name = "Mixed",
        tagline = "A bit of every style",
        description = "Variety-focused week built from mixed circuits that combine 2–3 moves from each " +
            "training style, with core and recovery. Great if you get bored easily.",
        accent = WorkoutType.FULL_BODY,
        days = listOf(
            WorkoutDay(DayOfWeek.MONDAY, "Mixed Circuit", mixedCircuit),
            WorkoutDay(DayOfWeek.TUESDAY, "Core & Abs", coreCrusher),
            WorkoutDay(DayOfWeek.WEDNESDAY, "Mixed Express", mixedExpress),
            WorkoutDay(DayOfWeek.THURSDAY, "Mobility & Stretch", activeRecovery),
            WorkoutDay(DayOfWeek.FRIDAY, "Mixed Circuit", mixedCircuit),
            WorkoutDay(DayOfWeek.SATURDAY, "Cardio Burner", cardioBurner),
            rest(DayOfWeek.SUNDAY)
        )
    )

    val programs: List<WeeklyProgram> =
        listOf(balanced, hiitProgram, strengthPPL, broSplit, mixedProgram)

    const val DEFAULT_PROGRAM_ID = "balanced"
    const val CUSTOM_PROGRAM_ID = "custom"

    fun programById(id: String?): WeeklyProgram =
        programs.firstOrNull { it.id == id } ?: balanced

    /** Backwards-compatible accessor for the default program's weekly days. */
    val weeklyPlan: List<WorkoutDay> get() = balanced.days

    fun dayFor(dayOfWeek: DayOfWeek): WorkoutDay = balanced.dayFor(dayOfWeek)
}
