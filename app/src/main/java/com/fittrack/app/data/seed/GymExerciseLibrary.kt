package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Exercise
import com.fittrack.app.data.model.WorkoutType

/**
 * Gym / equipment-based exercises used by the strength and single-muscle-split
 * programs. Each entry still includes a plain description, step-by-step how-to,
 * form tips and target muscles. Kept separate from the bodyweight
 * [ExerciseLibrary] for readability; both are merged in [ExerciseLibrary.all].
 */
object GymExerciseLibrary {

    val all: List<Exercise> by lazy {
        listOf(
            // Chest
            benchPress, inclineDbPress, chestFly,
            // Back
            pullUp, bentOverRow, latPulldown, seatedRow, deadlift,
            // Shoulders
            overheadPress, lateralRaise, frontRaise, rearDeltFly, shrug,
            // Arms
            barbellCurl, hammerCurl, tricepPushdown, overheadTricepExt,
            // Legs
            barbellSquat, romanianDeadlift, legPress, legCurl, legExtension
        )
    }

    // ----------------------------- Chest -----------------------------

    val benchPress = Exercise(
        id = "bench_press",
        name = "Barbell Bench Press",
        category = WorkoutType.STRENGTH,
        description = "The classic horizontal press and the main builder of chest, front-shoulder and triceps strength.",
        instructions = listOf(
            "Lie flat on the bench with your eyes under the bar and feet planted.",
            "Grip the bar slightly wider than shoulder-width and unrack it over your chest.",
            "Lower the bar under control to the middle of your chest, elbows about 45 degrees.",
            "Touch the chest lightly, then press the bar back up to straight arms.",
            "Keep your shoulder blades pinched and your glutes on the bench throughout."
        ),
        targetMuscles = listOf("Chest", "Triceps", "Front shoulders"),
        equipment = "Barbell + bench",
        sets = 4,
        reps = "6–10 reps",
        restSeconds = 120,
        tips = listOf("Always use a spotter or safety pins for heavy sets.", "Keep your wrists stacked over your elbows.")
    )

    val inclineDbPress = Exercise(
        id = "incline_db_press",
        name = "Incline Dumbbell Press",
        category = WorkoutType.STRENGTH,
        description = "An incline press that emphasises the upper chest and front shoulders.",
        instructions = listOf(
            "Set a bench to about a 30–45 degree incline and sit back with a dumbbell in each hand.",
            "Start with the dumbbells at the sides of your upper chest, palms facing forward.",
            "Press the dumbbells up and slightly together until your arms are straight.",
            "Lower them under control back to chest level.",
            "Keep your core tight and back against the bench."
        ),
        targetMuscles = listOf("Upper chest", "Front shoulders", "Triceps"),
        equipment = "Dumbbells + incline bench",
        sets = 3,
        reps = "8–12 reps",
        restSeconds = 90,
        tips = listOf("Don't clang the dumbbells together at the top.", "Control the lowering for more growth.")
    )

    val chestFly = Exercise(
        id = "chest_fly",
        name = "Dumbbell Chest Fly",
        category = WorkoutType.STRENGTH,
        description = "An isolation move that stretches and contracts the chest through a wide arc.",
        instructions = listOf(
            "Lie on a flat bench holding a dumbbell in each hand above your chest.",
            "Keep a slight, fixed bend in your elbows.",
            "Open your arms out wide in an arc until you feel a stretch across your chest.",
            "Squeeze your chest to bring the dumbbells back together over your chest.",
            "Keep the elbow bend constant — don't turn it into a press."
        ),
        targetMuscles = listOf("Chest"),
        equipment = "Dumbbells + bench",
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 60,
        tips = listOf("Use lighter weight than you press with.", "Think 'hugging a tree'.")
    )

    // ----------------------------- Back -----------------------------

    val pullUp = Exercise(
        id = "pull_up",
        name = "Pull-Up",
        category = WorkoutType.STRENGTH,
        description = "The premier upper-back and lat builder, lifting your bodyweight to the bar.",
        instructions = listOf(
            "Hang from a bar with an overhand grip slightly wider than shoulders.",
            "Start from a full hang with arms straight and shoulders engaged.",
            "Pull your chest toward the bar by driving your elbows down and back.",
            "Continue until your chin clears the bar.",
            "Lower under control back to a full hang."
        ),
        targetMuscles = listOf("Lats", "Upper back", "Biceps"),
        equipment = "Pull-up bar",
        sets = 3,
        reps = "5–10 reps",
        restSeconds = 90,
        tips = listOf("Use a resistance band or assisted machine if you can't do one yet.", "Avoid swinging or kipping.")
    )

    val bentOverRow = Exercise(
        id = "bent_over_row",
        name = "Bent-Over Barbell Row",
        category = WorkoutType.STRENGTH,
        description = "A horizontal pull that builds a thick, strong mid-back.",
        instructions = listOf(
            "Stand holding a barbell with an overhand grip, hands shoulder-width.",
            "Hinge at the hips until your torso is about 45 degrees, back flat.",
            "Let the bar hang at arms' length below your chest.",
            "Pull the bar to your lower ribs, squeezing your shoulder blades together.",
            "Lower under control and keep your back flat the whole set."
        ),
        targetMuscles = listOf("Mid back", "Lats", "Rear shoulders", "Biceps"),
        equipment = "Barbell",
        sets = 4,
        reps = "8–12 reps",
        restSeconds = 90,
        tips = listOf("Keep your core braced to protect your lower back.", "Don't use momentum to heave the weight.")
    )

    val latPulldown = Exercise(
        id = "lat_pulldown",
        name = "Lat Pulldown",
        category = WorkoutType.STRENGTH,
        description = "A cable pull-down that targets the lats — a great pull-up alternative.",
        instructions = listOf(
            "Sit at the lat pulldown machine and grip the bar wider than shoulders.",
            "Secure your thighs under the pad and sit tall.",
            "Pull the bar down to your upper chest, driving your elbows down.",
            "Squeeze your back at the bottom.",
            "Let the bar rise under control until your arms are straight."
        ),
        targetMuscles = listOf("Lats", "Upper back", "Biceps"),
        equipment = "Cable machine",
        sets = 3,
        reps = "10–12 reps",
        restSeconds = 75,
        tips = listOf("Don't lean way back — keep the torso fairly upright.", "Avoid pulling behind your neck.")
    )

    val seatedRow = Exercise(
        id = "seated_row",
        name = "Seated Cable Row",
        category = WorkoutType.STRENGTH,
        description = "A seated horizontal cable pull that develops mid-back thickness and posture.",
        instructions = listOf(
            "Sit at the cable row station and place your feet on the platform.",
            "Grab the handle and sit tall with a slight bend in your knees.",
            "Pull the handle to your stomach, driving your elbows back.",
            "Squeeze your shoulder blades together at the end.",
            "Extend your arms forward under control without rounding your back."
        ),
        targetMuscles = listOf("Mid back", "Lats", "Biceps"),
        equipment = "Cable machine",
        sets = 3,
        reps = "10–12 reps",
        restSeconds = 75,
        tips = listOf("Keep your chest up throughout.", "Don't rock your whole torso to move the weight.")
    )

    val deadlift = Exercise(
        id = "deadlift",
        name = "Barbell Deadlift",
        category = WorkoutType.STRENGTH,
        description = "A full-body pull from the floor that builds the entire posterior chain and overall strength.",
        instructions = listOf(
            "Stand with mid-foot under the bar, feet hip-width apart.",
            "Hinge down and grip the bar just outside your knees.",
            "Set a flat back, chest up, and take the slack out of the bar.",
            "Drive through your feet and stand up, keeping the bar close to your legs.",
            "Lock out at the top, then lower under control by hinging the hips back."
        ),
        targetMuscles = listOf("Hamstrings", "Glutes", "Lower back", "Traps"),
        equipment = "Barbell",
        sets = 3,
        reps = "5–8 reps",
        restSeconds = 150,
        tips = listOf("Never round your lower back.", "Push the floor away rather than yanking the bar.")
    )

    // ----------------------------- Shoulders -----------------------------

    val overheadPress = Exercise(
        id = "overhead_press",
        name = "Overhead Press",
        category = WorkoutType.STRENGTH,
        description = "A standing vertical press that builds strong, capped shoulders and a stable core.",
        instructions = listOf(
            "Stand holding a barbell (or dumbbells) at shoulder height, hands just outside shoulders.",
            "Brace your core and squeeze your glutes.",
            "Press the weight straight overhead until your arms lock out.",
            "Move your head slightly back so the bar travels in a straight line.",
            "Lower under control back to your shoulders."
        ),
        targetMuscles = listOf("Shoulders", "Triceps", "Upper chest"),
        equipment = "Barbell or dumbbells",
        sets = 4,
        reps = "6–10 reps",
        restSeconds = 90,
        tips = listOf("Don't lean back excessively — keep ribs down.", "Keep the bar over your mid-foot.")
    )

    val lateralRaise = Exercise(
        id = "lateral_raise",
        name = "Dumbbell Lateral Raise",
        category = WorkoutType.STRENGTH,
        description = "An isolation move that targets the side delts to build shoulder width.",
        instructions = listOf(
            "Stand holding a dumbbell in each hand at your sides.",
            "Keep a slight bend in your elbows.",
            "Raise the dumbbells out to the sides until they reach shoulder height.",
            "Lead with your elbows, keeping your pinkies slightly up.",
            "Lower slowly back to your sides."
        ),
        targetMuscles = listOf("Side shoulders"),
        equipment = "Dumbbells",
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 60,
        tips = listOf("Use light weight and strict form — no swinging.", "Don't shrug your traps up.")
    )

    val frontRaise = Exercise(
        id = "front_raise",
        name = "Dumbbell Front Raise",
        category = WorkoutType.STRENGTH,
        description = "An isolation move for the front delts.",
        instructions = listOf(
            "Stand holding dumbbells in front of your thighs, palms facing you.",
            "Keep your arms nearly straight with a slight elbow bend.",
            "Raise one or both dumbbells forward to shoulder height.",
            "Pause briefly at the top.",
            "Lower under control and repeat."
        ),
        targetMuscles = listOf("Front shoulders"),
        equipment = "Dumbbells",
        sets = 3,
        reps = "12 reps",
        restSeconds = 60,
        tips = listOf("Avoid leaning back to swing the weight.", "Keep the movement smooth.")
    )

    val rearDeltFly = Exercise(
        id = "rear_delt_fly",
        name = "Rear Delt Fly",
        category = WorkoutType.STRENGTH,
        description = "A reverse fly that targets the often-neglected rear delts for balanced shoulders and posture.",
        instructions = listOf(
            "Hold a dumbbell in each hand and hinge forward at the hips, back flat.",
            "Let the dumbbells hang below your chest with a slight elbow bend.",
            "Raise your arms out to the sides, squeezing your rear shoulders.",
            "Stop when your arms are roughly parallel to the floor.",
            "Lower under control."
        ),
        targetMuscles = listOf("Rear shoulders", "Upper back"),
        equipment = "Dumbbells",
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 60,
        tips = listOf("Use light weight and focus on the squeeze.", "Keep your neck relaxed.")
    )

    val shrug = Exercise(
        id = "shrug",
        name = "Dumbbell Shrug",
        category = WorkoutType.STRENGTH,
        description = "A simple move that builds the upper traps.",
        instructions = listOf(
            "Stand tall holding a dumbbell in each hand at your sides.",
            "Keep your arms straight.",
            "Shrug your shoulders straight up toward your ears.",
            "Pause and squeeze at the top.",
            "Lower under control — don't roll your shoulders."
        ),
        targetMuscles = listOf("Traps"),
        equipment = "Dumbbells",
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 60,
        tips = listOf("Lift straight up and down, not in circles.", "Hold the top squeeze for a second.")
    )

    // ----------------------------- Arms -----------------------------

    val barbellCurl = Exercise(
        id = "barbell_curl",
        name = "Barbell Biceps Curl",
        category = WorkoutType.STRENGTH,
        description = "The staple biceps mass builder.",
        instructions = listOf(
            "Stand holding a barbell with an underhand, shoulder-width grip.",
            "Keep your elbows tucked at your sides.",
            "Curl the bar up toward your shoulders by bending the elbows.",
            "Squeeze your biceps at the top.",
            "Lower under control until your arms are straight."
        ),
        targetMuscles = listOf("Biceps", "Forearms"),
        equipment = "Barbell",
        sets = 3,
        reps = "8–12 reps",
        restSeconds = 60,
        tips = listOf("Keep your elbows still — don't swing them forward.", "Avoid using your back to heave the bar.")
    )

    val hammerCurl = Exercise(
        id = "hammer_curl",
        name = "Hammer Curl",
        category = WorkoutType.STRENGTH,
        description = "A neutral-grip curl that hits the biceps and the brachialis for thicker arms.",
        instructions = listOf(
            "Stand holding dumbbells at your sides with palms facing each other.",
            "Keep your elbows tucked in.",
            "Curl the dumbbells up while keeping the neutral (thumbs-up) grip.",
            "Squeeze at the top.",
            "Lower under control."
        ),
        targetMuscles = listOf("Biceps", "Brachialis", "Forearms"),
        equipment = "Dumbbells",
        sets = 3,
        reps = "10–12 reps",
        restSeconds = 60,
        tips = listOf("Don't rotate the wrist — keep palms facing in.", "Control the lowering phase.")
    )

    val tricepPushdown = Exercise(
        id = "tricep_pushdown",
        name = "Cable Triceps Pushdown",
        category = WorkoutType.STRENGTH,
        description = "A cable isolation move for the triceps.",
        instructions = listOf(
            "Stand at a cable machine with a bar or rope attached high.",
            "Grip the attachment and tuck your elbows at your sides.",
            "Push the attachment down until your arms are fully straight.",
            "Squeeze your triceps at the bottom.",
            "Let it rise under control to about 90 degrees and repeat."
        ),
        targetMuscles = listOf("Triceps"),
        equipment = "Cable machine",
        sets = 3,
        reps = "10–15 reps",
        restSeconds = 60,
        tips = listOf("Keep your elbows pinned — only your forearms move.", "Don't lean over the bar.")
    )

    val overheadTricepExt = Exercise(
        id = "overhead_tricep_ext",
        name = "Overhead Triceps Extension",
        category = WorkoutType.STRENGTH,
        description = "An overhead extension that stretches and works the long head of the triceps.",
        instructions = listOf(
            "Hold one dumbbell with both hands overhead, arms extended.",
            "Keep your elbows pointing forward and close to your head.",
            "Lower the dumbbell behind your head by bending the elbows.",
            "Stop when you feel a stretch, then extend back to the top.",
            "Keep your upper arms still throughout."
        ),
        targetMuscles = listOf("Triceps"),
        equipment = "Dumbbell",
        sets = 3,
        reps = "10–12 reps",
        restSeconds = 60,
        tips = listOf("Keep your core braced so you don't arch your back.", "Move only at the elbows.")
    )

    // ----------------------------- Legs -----------------------------

    val barbellSquat = Exercise(
        id = "barbell_squat",
        name = "Barbell Back Squat",
        category = WorkoutType.STRENGTH,
        description = "The king of leg exercises, loading the quads, glutes and hamstrings under a barbell.",
        instructions = listOf(
            "Set the bar on your upper back and unrack it, stepping back with feet shoulder-width.",
            "Brace your core and keep your chest up.",
            "Push your hips back and bend your knees to descend.",
            "Lower until your thighs are at least parallel to the floor.",
            "Drive through your whole foot to stand back up."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings", "Core"),
        equipment = "Barbell + squat rack",
        sets = 4,
        reps = "6–10 reps",
        restSeconds = 150,
        tips = listOf("Use safety pins set at the right height.", "Keep your knees tracking over your toes.")
    )

    val romanianDeadlift = Exercise(
        id = "romanian_deadlift",
        name = "Romanian Deadlift",
        category = WorkoutType.STRENGTH,
        description = "A hip-hinge that targets the hamstrings and glutes through a strong stretch.",
        instructions = listOf(
            "Stand holding a barbell at your thighs, feet hip-width apart.",
            "Keep a soft bend in your knees and a flat back.",
            "Push your hips back and slide the bar down the front of your legs.",
            "Lower until you feel a strong stretch in your hamstrings.",
            "Drive your hips forward to return to standing."
        ),
        targetMuscles = listOf("Hamstrings", "Glutes", "Lower back"),
        equipment = "Barbell",
        sets = 3,
        reps = "8–12 reps",
        restSeconds = 90,
        tips = listOf("Keep the bar close to your body.", "Hinge at the hips, don't squat down.")
    )

    val legPress = Exercise(
        id = "leg_press",
        name = "Leg Press",
        category = WorkoutType.STRENGTH,
        description = "A machine press that loads the quads and glutes with the back supported.",
        instructions = listOf(
            "Sit in the leg press machine with feet shoulder-width on the platform.",
            "Release the safeties and hold the platform with your legs.",
            "Lower the platform by bending your knees toward your chest.",
            "Stop when your knees reach about 90 degrees.",
            "Press the platform back up without locking your knees harshly."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings"),
        equipment = "Leg press machine",
        sets = 3,
        reps = "10–15 reps",
        restSeconds = 90,
        tips = listOf("Don't let your lower back round off the seat.", "Keep your knees in line with your toes.")
    )

    val legCurl = Exercise(
        id = "leg_curl",
        name = "Lying Leg Curl",
        category = WorkoutType.STRENGTH,
        description = "A machine isolation move for the hamstrings.",
        instructions = listOf(
            "Lie face down on the leg curl machine with the pad on your lower calves.",
            "Grip the handles and keep your hips pressed down.",
            "Curl your heels toward your glutes by bending the knees.",
            "Squeeze your hamstrings at the top.",
            "Lower under control to the start."
        ),
        targetMuscles = listOf("Hamstrings"),
        equipment = "Leg curl machine",
        sets = 3,
        reps = "10–15 reps",
        restSeconds = 60,
        tips = listOf("Avoid lifting your hips off the pad.", "Control the negative for best results.")
    )

    val legExtension = Exercise(
        id = "leg_extension",
        name = "Leg Extension",
        category = WorkoutType.STRENGTH,
        description = "A machine isolation move that targets the quadriceps.",
        instructions = listOf(
            "Sit in the leg extension machine with the pad on your lower shins.",
            "Grip the handles and sit back into the seat.",
            "Extend your knees to raise the pad until your legs are straight.",
            "Squeeze your quads at the top.",
            "Lower under control to the start."
        ),
        targetMuscles = listOf("Quads"),
        equipment = "Leg extension machine",
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 60,
        tips = listOf("Don't swing the weight — move with control.", "Pause at the top for a stronger contraction.")
    )
}
