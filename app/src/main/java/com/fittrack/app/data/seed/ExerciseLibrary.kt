package com.fittrack.app.data.seed

import com.fittrack.app.data.model.Exercise
import com.fittrack.app.data.model.WorkoutType

/**
 * Static, offline library of exercises. Every entry includes a plain-language
 * description plus ordered, step-by-step instructions on how to perform it
 * safely. No network or paid service is required.
 */
object ExerciseLibrary {

    val all: List<Exercise> by lazy {
        listOf(
            jumpingJacks, highKnees, burpees, mountainClimbers, jumpSquats, skaters,
            buttKicks, plankJacks, squatThrust, tuckJump,
            pushUp, squat, lunge, gluteBridge, supermanRaise, wallSit, calfRaise,
            pikePushUp, tricepDip, reverseLunge, singleLegDeadlift, inchworm,
            diamondPushUp, bulgarianSplitSquat, stepUp,
            plank, sidePlank, bicycleCrunch, deadBug, legRaise, russianTwist,
            hollowHold, flutterKicks, vUp, reverseCrunch, plankShoulderTap, birdDog,
            jogInPlace, marchInPlace, fastFeet, shadowBoxing,
            childPose, catCow, downwardDog, worldsGreatest, hamstringStretch,
            chestOpener, armCircles, cobraStretch, lungeWithTwist
        ) + GymExerciseLibrary.all
    }

    private fun byId(id: String): Exercise =
        all.firstOrNull { it.id == id } ?: error("Unknown exercise id: $id")

    fun get(id: String): Exercise = byId(id)

    // ---------------------------------------------------------------------
    // HIIT / Cardio conditioning
    // ---------------------------------------------------------------------

    val jumpingJacks = Exercise(
        id = "jumping_jacks",
        name = "Jumping Jacks",
        category = WorkoutType.HIIT,
        description = "A classic full-body cardio move that raises your heart rate and warms up the whole body.",
        instructions = listOf(
            "Stand upright with your feet together and arms relaxed at your sides.",
            "Bend your knees slightly, then jump up.",
            "As you jump, spread your legs to about shoulder-width and swing your arms overhead.",
            "Jump again to bring your feet back together and arms back to your sides.",
            "Land softly on the balls of your feet and keep a steady rhythm."
        ),
        targetMuscles = listOf("Full body", "Shoulders", "Calves"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Keep your core gently braced.", "Breathe in a steady rhythm — don't hold your breath.")
    )

    val highKnees = Exercise(
        id = "high_knees",
        name = "High Knees",
        category = WorkoutType.HIIT,
        description = "A running-in-place drill that drives the knees up high to spike your heart rate and engage the core.",
        instructions = listOf(
            "Stand tall with feet hip-width apart and arms bent at 90 degrees.",
            "Drive your right knee up toward your chest while pumping your left arm.",
            "Quickly switch, driving the left knee up and pumping the right arm.",
            "Continue alternating as fast as you can control while staying tall.",
            "Stay on the balls of your feet and land softly."
        ),
        targetMuscles = listOf("Hip flexors", "Quads", "Core", "Calves"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Aim to bring knees to hip height.", "Keep your chest up and avoid leaning back.")
    )

    val burpees = Exercise(
        id = "burpees",
        name = "Burpees",
        category = WorkoutType.HIIT,
        description = "A demanding full-body exercise combining a squat, plank, push-up and jump — great for conditioning and strength.",
        instructions = listOf(
            "Stand with feet shoulder-width apart.",
            "Squat down and place both hands on the floor in front of you.",
            "Jump or step both feet back into a high plank position.",
            "Optionally perform one push-up, keeping your body in a straight line.",
            "Jump or step your feet back toward your hands.",
            "Explode upward into a jump, reaching your arms overhead.",
            "Land softly and immediately lower into the next rep."
        ),
        targetMuscles = listOf("Full body", "Chest", "Legs", "Core"),
        workSeconds = 30,
        restSeconds = 30,
        tips = listOf(
            "Beginners: step the feet back and skip the jump.",
            "Keep your back flat in the plank — don't let your hips sag."
        )
    )

    val mountainClimbers = Exercise(
        id = "mountain_climbers",
        name = "Mountain Climbers",
        category = WorkoutType.HIIT,
        description = "A plank-based cardio move where you rapidly drive the knees toward the chest, working the core and shoulders.",
        instructions = listOf(
            "Start in a high plank: hands under shoulders, body in a straight line.",
            "Brace your core and keep your hips level.",
            "Drive your right knee toward your chest, then return it.",
            "Immediately drive your left knee toward your chest.",
            "Continue alternating quickly, like running in a plank position."
        ),
        targetMuscles = listOf("Core", "Shoulders", "Hip flexors"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Keep shoulders stacked over your wrists.", "Avoid bouncing your hips up and down.")
    )

    val jumpSquats = Exercise(
        id = "jump_squats",
        name = "Jump Squats",
        category = WorkoutType.HIIT,
        description = "An explosive lower-body move that builds power in the legs and elevates the heart rate.",
        instructions = listOf(
            "Stand with feet shoulder-width apart, toes slightly out.",
            "Lower into a squat, pushing your hips back and bending your knees.",
            "Drive through your feet and explode upward into a jump.",
            "Reach full extension in the air, then land softly with bent knees.",
            "Immediately lower into the next squat."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings", "Calves"),
        workSeconds = 30,
        restSeconds = 30,
        tips = listOf("Land quietly on the whole foot.", "Keep your chest up throughout.")
    )

    val skaters = Exercise(
        id = "skaters",
        name = "Skater Hops",
        category = WorkoutType.HIIT,
        description = "Lateral bounding from side to side that builds single-leg power, balance and cardio fitness.",
        instructions = listOf(
            "Start standing on your right leg with a soft knee bend.",
            "Push off your right foot and leap laterally to your left.",
            "Land on your left foot, sweeping your right leg behind you.",
            "Reach your right hand toward the floor for balance if needed.",
            "Immediately bound back to the right side and keep alternating."
        ),
        targetMuscles = listOf("Glutes", "Quads", "Hamstrings", "Calves"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Stick each landing for a beat to build balance.", "Drive with the outside leg.")
    )

    val buttKicks = Exercise(
        id = "butt_kicks",
        name = "Butt Kicks",
        category = WorkoutType.CARDIO,
        description = "A jogging-in-place drill that flicks the heels toward the glutes, warming up the hamstrings.",
        instructions = listOf(
            "Stand tall with feet hip-width apart.",
            "Jog in place, kicking your right heel up toward your glutes.",
            "Switch quickly, kicking your left heel up.",
            "Pump your arms as if running and keep a quick cadence."
        ),
        targetMuscles = listOf("Hamstrings", "Calves"),
        workSeconds = 30,
        restSeconds = 15,
        tips = listOf("Stay light on your feet.", "Keep your torso upright.")
    )

    val plankJacks = Exercise(
        id = "plank_jacks",
        name = "Plank Jacks",
        category = WorkoutType.HIIT,
        description = "A plank hold combined with a jumping-jack motion of the legs to challenge the core and shoulders.",
        instructions = listOf(
            "Begin in a forearm or high plank with feet together.",
            "Brace your core and keep your hips level.",
            "Jump both feet out wide, like the bottom of a jumping jack.",
            "Jump both feet back together.",
            "Keep a steady rhythm without letting your hips rise or sag."
        ),
        targetMuscles = listOf("Core", "Shoulders", "Glutes"),
        workSeconds = 30,
        restSeconds = 20,
        tips = listOf("Smaller jumps keep the core more engaged.", "Keep your neck neutral.")
    )

    // ---------------------------------------------------------------------
    // Strength (bodyweight)
    // ---------------------------------------------------------------------

    val pushUp = Exercise(
        id = "push_up",
        name = "Push-Up",
        category = WorkoutType.STRENGTH,
        description = "A foundational upper-body press that builds the chest, shoulders, triceps and core.",
        instructions = listOf(
            "Start in a high plank with hands slightly wider than shoulders.",
            "Set your body in a straight line from head to heels and brace your core.",
            "Bend your elbows to lower your chest toward the floor, elbows at about 45 degrees.",
            "Lower until your chest is just above the ground.",
            "Press through your palms to push back up to the start."
        ),
        targetMuscles = listOf("Chest", "Triceps", "Front shoulders", "Core"),
        sets = 3,
        reps = "8–15 reps",
        restSeconds = 60,
        tips = listOf(
            "Too hard? Drop to your knees or push up against a wall.",
            "Keep your hips from sagging by squeezing your glutes."
        )
    )

    val squat = Exercise(
        id = "squat",
        name = "Bodyweight Squat",
        category = WorkoutType.STRENGTH,
        description = "The king of lower-body exercises, training the quads, glutes and hamstrings.",
        instructions = listOf(
            "Stand with feet shoulder-width apart, toes turned slightly out.",
            "Brace your core and keep your chest tall.",
            "Push your hips back and bend your knees as if sitting into a chair.",
            "Lower until your thighs are roughly parallel to the floor.",
            "Drive through your whole foot to stand back up, squeezing your glutes at the top."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings"),
        sets = 3,
        reps = "12–20 reps",
        restSeconds = 60,
        tips = listOf("Keep your knees tracking over your toes.", "Keep your heels planted.")
    )

    val lunge = Exercise(
        id = "lunge",
        name = "Forward Lunge",
        category = WorkoutType.STRENGTH,
        description = "A single-leg movement that builds leg strength and balance while ironing out left/right imbalances.",
        instructions = listOf(
            "Stand tall with feet hip-width apart, hands on your hips.",
            "Step forward with your right foot.",
            "Lower your hips until both knees are bent to about 90 degrees.",
            "Keep your front knee over your ankle and your back knee just above the floor.",
            "Push through your front foot to return to standing, then switch legs."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings"),
        sets = 3,
        reps = "10 reps per leg",
        restSeconds = 60,
        tips = listOf("Keep your torso upright.", "Take a big enough step to keep the knee safe.")
    )

    val reverseLunge = Exercise(
        id = "reverse_lunge",
        name = "Reverse Lunge",
        category = WorkoutType.STRENGTH,
        description = "A knee-friendlier lunge variation where you step backward, emphasising the glutes.",
        instructions = listOf(
            "Stand tall with feet hip-width apart.",
            "Step your right foot back behind you.",
            "Lower until both knees are bent to about 90 degrees.",
            "Keep most of your weight on your front foot.",
            "Drive through the front heel to return to standing, then alternate."
        ),
        targetMuscles = listOf("Glutes", "Quads", "Hamstrings"),
        sets = 3,
        reps = "10 reps per leg",
        restSeconds = 60,
        tips = listOf("Control the descent — don't crash the back knee down.", "Keep your chest proud.")
    )

    val gluteBridge = Exercise(
        id = "glute_bridge",
        name = "Glute Bridge",
        category = WorkoutType.STRENGTH,
        description = "A floor exercise that isolates and strengthens the glutes while protecting the lower back.",
        instructions = listOf(
            "Lie on your back with knees bent and feet flat, hip-width apart.",
            "Rest your arms by your sides, palms down.",
            "Squeeze your glutes and press through your heels to lift your hips.",
            "Raise until your body forms a straight line from knees to shoulders.",
            "Pause and squeeze at the top, then lower with control."
        ),
        targetMuscles = listOf("Glutes", "Hamstrings", "Core"),
        sets = 3,
        reps = "15 reps",
        restSeconds = 45,
        tips = listOf("Avoid arching your lower back — the movement comes from the hips.", "Keep your ribs down.")
    )

    val supermanRaise = Exercise(
        id = "superman",
        name = "Superman",
        category = WorkoutType.STRENGTH,
        description = "A back-extension move that strengthens the lower back, glutes and rear shoulders.",
        instructions = listOf(
            "Lie face down with arms extended overhead and legs straight.",
            "Brace your core gently.",
            "Simultaneously lift your arms, chest and legs off the floor.",
            "Hold for a moment, squeezing your lower back and glutes.",
            "Lower back down with control."
        ),
        targetMuscles = listOf("Lower back", "Glutes", "Rear shoulders"),
        sets = 3,
        reps = "12 reps",
        restSeconds = 45,
        tips = listOf("Look at the floor to keep your neck neutral.", "Lift smoothly, don't jerk.")
    )

    val wallSit = Exercise(
        id = "wall_sit",
        name = "Wall Sit",
        category = WorkoutType.STRENGTH,
        description = "An isometric hold against a wall that builds quad endurance and mental toughness.",
        instructions = listOf(
            "Stand with your back against a wall, feet about two feet out.",
            "Slide down the wall until your thighs are parallel to the floor.",
            "Keep your knees directly above your ankles at 90 degrees.",
            "Press your back flat against the wall and hold the position.",
            "Breathe steadily for the full duration."
        ),
        targetMuscles = listOf("Quads", "Glutes"),
        workSeconds = 40,
        restSeconds = 30,
        tips = listOf("Keep your weight in your heels.", "Don't rest your hands on your thighs.")
    )

    val calfRaise = Exercise(
        id = "calf_raise",
        name = "Calf Raise",
        category = WorkoutType.STRENGTH,
        description = "A simple move that strengthens the calves and improves ankle stability.",
        instructions = listOf(
            "Stand tall with feet hip-width apart, near a wall for balance.",
            "Press through the balls of your feet to raise your heels as high as possible.",
            "Pause at the top and squeeze your calves.",
            "Lower your heels slowly back to the floor.",
            "Repeat with a smooth, controlled tempo."
        ),
        targetMuscles = listOf("Calves"),
        sets = 3,
        reps = "20 reps",
        restSeconds = 30,
        tips = listOf("For more challenge, do one leg at a time.", "Pause at the top for a stronger contraction.")
    )

    val pikePushUp = Exercise(
        id = "pike_push_up",
        name = "Pike Push-Up",
        category = WorkoutType.STRENGTH,
        description = "An inverted push-up variation that shifts the load onto the shoulders, building overhead pressing strength.",
        instructions = listOf(
            "Start in a downward-dog shape: hips high, body in an upside-down V.",
            "Place hands shoulder-width apart and walk your feet slightly in.",
            "Bend your elbows to lower the crown of your head toward the floor.",
            "Stop just before your head touches, keeping your hips high.",
            "Press back up to the starting position."
        ),
        targetMuscles = listOf("Shoulders", "Triceps", "Upper chest"),
        sets = 3,
        reps = "6–12 reps",
        restSeconds = 60,
        tips = listOf("The higher your hips, the more shoulder-focused it is.", "Keep your core tight.")
    )

    val tricepDip = Exercise(
        id = "tricep_dip",
        name = "Triceps Dip",
        category = WorkoutType.STRENGTH,
        description = "A pressing move using a chair or step that targets the back of the arms.",
        instructions = listOf(
            "Sit on the edge of a sturdy chair or step and grip the edge beside your hips.",
            "Slide your hips forward off the seat, supporting your weight on your hands.",
            "Keep your elbows pointing straight back.",
            "Bend your elbows to lower your hips toward the floor.",
            "Stop when your elbows reach about 90 degrees, then press back up."
        ),
        targetMuscles = listOf("Triceps", "Front shoulders", "Chest"),
        equipment = "Sturdy chair, bench or step",
        sets = 3,
        reps = "10–15 reps",
        restSeconds = 60,
        tips = listOf("Keep your back close to the chair.", "Bend your knees more to make it easier.")
    )

    val singleLegDeadlift = Exercise(
        id = "single_leg_deadlift",
        name = "Single-Leg Deadlift",
        category = WorkoutType.STRENGTH,
        description = "A balance and posterior-chain move that strengthens the hamstrings and glutes one leg at a time.",
        instructions = listOf(
            "Stand on your right leg with a soft knee bend.",
            "Keeping your back flat, hinge forward at the hips.",
            "Let your left leg extend straight behind you as a counterbalance.",
            "Lower your hands toward the floor until you feel a hamstring stretch.",
            "Squeeze your glute to return to standing, then switch legs."
        ),
        targetMuscles = listOf("Hamstrings", "Glutes", "Core"),
        sets = 3,
        reps = "8 reps per leg",
        restSeconds = 45,
        tips = listOf("Move slowly to stay balanced.", "Keep your hips square to the floor.")
    )

    val inchworm = Exercise(
        id = "inchworm",
        name = "Inchworm",
        category = WorkoutType.FLEXIBILITY,
        description = "A dynamic warm-up that stretches the hamstrings and warms the core and shoulders.",
        instructions = listOf(
            "Stand tall, then hinge forward and place your hands on the floor.",
            "Walk your hands forward until you reach a high plank.",
            "Hold the plank for a second, keeping your core tight.",
            "Walk your feet toward your hands in small steps.",
            "Stand back up and repeat."
        ),
        targetMuscles = listOf("Hamstrings", "Core", "Shoulders"),
        sets = 2,
        reps = "6 reps",
        restSeconds = 30,
        tips = listOf("Bend your knees as needed to reach the floor.", "Move with control.")
    )

    // ---------------------------------------------------------------------
    // Core
    // ---------------------------------------------------------------------

    val plank = Exercise(
        id = "plank",
        name = "Forearm Plank",
        category = WorkoutType.CORE,
        description = "An isometric hold that builds deep core stability and a strong, braced trunk.",
        instructions = listOf(
            "Place your forearms on the floor with elbows under your shoulders.",
            "Extend your legs back and rise onto your toes.",
            "Form a straight line from your head to your heels.",
            "Brace your core and squeeze your glutes.",
            "Hold the position while breathing steadily."
        ),
        targetMuscles = listOf("Core", "Shoulders", "Glutes"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Don't let your hips sag or pike up.", "Keep your neck long, gaze at the floor.")
    )

    val sidePlank = Exercise(
        id = "side_plank",
        name = "Side Plank",
        category = WorkoutType.CORE,
        description = "A lateral core hold that targets the obliques and improves trunk stability.",
        instructions = listOf(
            "Lie on your side with your forearm on the floor, elbow under your shoulder.",
            "Stack your feet and legs on top of each other.",
            "Lift your hips so your body forms a straight line.",
            "Reach your top arm toward the ceiling or rest it on your hip.",
            "Hold, then switch sides."
        ),
        targetMuscles = listOf("Obliques", "Core", "Shoulders"),
        workSeconds = 30,
        restSeconds = 15,
        tips = listOf("Drop the bottom knee to make it easier.", "Keep your hips lifted, don't let them drop.")
    )

    val bicycleCrunch = Exercise(
        id = "bicycle_crunch",
        name = "Bicycle Crunch",
        category = WorkoutType.CORE,
        description = "A rotational crunch that works the entire abdominal wall, especially the obliques.",
        instructions = listOf(
            "Lie on your back with hands lightly behind your head.",
            "Lift your shoulders off the floor and raise your legs to a tabletop position.",
            "Bring your right elbow toward your left knee while extending your right leg.",
            "Switch sides, bringing your left elbow toward your right knee.",
            "Continue pedaling in a slow, controlled motion."
        ),
        targetMuscles = listOf("Obliques", "Rectus abdominis", "Hip flexors"),
        sets = 3,
        reps = "20 total reps",
        restSeconds = 30,
        tips = listOf("Don't yank on your neck — your hands just cradle the head.", "Rotate from the torso, not the arms.")
    )

    val deadBug = Exercise(
        id = "dead_bug",
        name = "Dead Bug",
        category = WorkoutType.CORE,
        description = "A safe, controlled core exercise that teaches you to brace while moving the limbs.",
        instructions = listOf(
            "Lie on your back with arms reaching toward the ceiling.",
            "Lift your legs to a tabletop position, knees over hips.",
            "Press your lower back gently into the floor.",
            "Slowly lower your right arm overhead and extend your left leg.",
            "Return to the start and repeat on the opposite side."
        ),
        targetMuscles = listOf("Core", "Hip flexors"),
        sets = 3,
        reps = "10 reps per side",
        restSeconds = 30,
        tips = listOf("Keep your lower back glued to the floor the whole time.", "Move slowly and exhale as you extend.")
    )

    val legRaise = Exercise(
        id = "leg_raise",
        name = "Lying Leg Raise",
        category = WorkoutType.CORE,
        description = "A lower-ab focused move that lifts the legs to challenge the abdominals.",
        instructions = listOf(
            "Lie flat on your back with legs straight and hands under your glutes.",
            "Press your lower back into the floor.",
            "Keeping your legs straight, raise them until they point at the ceiling.",
            "Lower them slowly until just above the floor.",
            "Stop before your back arches, then raise again."
        ),
        targetMuscles = listOf("Lower abs", "Hip flexors"),
        sets = 3,
        reps = "12 reps",
        restSeconds = 30,
        tips = listOf("Bend your knees if your back lifts off the floor.", "Control the lowering phase.")
    )

    val russianTwist = Exercise(
        id = "russian_twist",
        name = "Russian Twist",
        category = WorkoutType.CORE,
        description = "A seated rotation that strongly targets the obliques and rotational strength.",
        instructions = listOf(
            "Sit on the floor with knees bent and feet flat or lifted.",
            "Lean back slightly to engage your core, keeping your back straight.",
            "Clasp your hands together in front of your chest.",
            "Rotate your torso to tap your hands to the floor on your right.",
            "Rotate to the left side and continue alternating."
        ),
        targetMuscles = listOf("Obliques", "Core"),
        sets = 3,
        reps = "20 total taps",
        restSeconds = 30,
        tips = listOf("Lift your feet to make it harder.", "Move from your waist, not just your arms.")
    )

    val hollowHold = Exercise(
        id = "hollow_hold",
        name = "Hollow Body Hold",
        category = WorkoutType.CORE,
        description = "A gymnastics staple that builds total core tension and control.",
        instructions = listOf(
            "Lie on your back and press your lower back into the floor.",
            "Extend your arms overhead and your legs out straight.",
            "Lift your shoulders, arms and legs a few inches off the floor.",
            "Form a shallow banana shape, keeping your back flat.",
            "Hold while breathing, keeping constant tension."
        ),
        targetMuscles = listOf("Core", "Hip flexors"),
        workSeconds = 25,
        restSeconds = 25,
        tips = listOf("Bend your knees or lower your arms to scale it down.", "Lower back must stay pressed down.")
    )

    val flutterKicks = Exercise(
        id = "flutter_kicks",
        name = "Flutter Kicks",
        category = WorkoutType.CORE,
        description = "Small, rapid leg flutters that build lower-ab endurance.",
        instructions = listOf(
            "Lie on your back with hands under your glutes for support.",
            "Lift both legs a few inches off the floor.",
            "Press your lower back into the floor.",
            "Flutter your legs up and down in small, quick scissor motions.",
            "Keep your legs straight and your core braced."
        ),
        targetMuscles = listOf("Lower abs", "Hip flexors"),
        workSeconds = 30,
        restSeconds = 20,
        tips = listOf("Raise your legs higher if your back arches.", "Keep the movements small and controlled.")
    )

    // ---------------------------------------------------------------------
    // Light cardio / warm-up
    // ---------------------------------------------------------------------

    val jogInPlace = Exercise(
        id = "jog_in_place",
        name = "Jog in Place",
        category = WorkoutType.CARDIO,
        description = "A gentle warm-up jog to gradually raise the heart rate.",
        instructions = listOf(
            "Stand tall with relaxed shoulders.",
            "Begin jogging on the spot, landing softly on the balls of your feet.",
            "Pump your arms naturally in time with your steps.",
            "Keep a comfortable, conversational pace."
        ),
        targetMuscles = listOf("Legs", "Cardiovascular system"),
        workSeconds = 60,
        restSeconds = 0,
        tips = listOf("Use this to warm up before harder work.", "Stay relaxed and breathe easily.")
    )

    val marchInPlace = Exercise(
        id = "march_in_place",
        name = "March in Place",
        category = WorkoutType.CARDIO,
        description = "A low-impact marching movement, ideal for warming up or active recovery.",
        instructions = listOf(
            "Stand tall with feet hip-width apart.",
            "Lift one knee to hip height, then place it down.",
            "Lift the opposite knee and continue marching.",
            "Swing your arms gently in opposition to your legs."
        ),
        targetMuscles = listOf("Legs", "Core"),
        workSeconds = 60,
        restSeconds = 0,
        tips = listOf("Great gentle option on recovery days.", "Keep your posture tall.")
    )

    // ---------------------------------------------------------------------
    // Mobility / flexibility
    // ---------------------------------------------------------------------

    val childPose = Exercise(
        id = "child_pose",
        name = "Child's Pose",
        category = WorkoutType.FLEXIBILITY,
        description = "A restful stretch that lengthens the back and hips and calms the body.",
        instructions = listOf(
            "Kneel on the floor with your big toes together and knees apart.",
            "Sit your hips back toward your heels.",
            "Walk your hands forward and lower your chest toward the floor.",
            "Rest your forehead on the mat and relax your arms.",
            "Breathe slowly and let your back relax."
        ),
        targetMuscles = listOf("Lower back", "Hips", "Shoulders"),
        workSeconds = 45,
        restSeconds = 0,
        tips = listOf("Widen your knees for a deeper hip stretch.", "Breathe into your back.")
    )

    val catCow = Exercise(
        id = "cat_cow",
        name = "Cat–Cow",
        category = WorkoutType.FLEXIBILITY,
        description = "A flowing spinal mobility drill that alternates between arching and rounding the back.",
        instructions = listOf(
            "Start on all fours with hands under shoulders and knees under hips.",
            "Inhale and drop your belly, lifting your chest and tailbone (Cow).",
            "Exhale and round your spine, tucking your chin and tailbone (Cat).",
            "Flow smoothly between the two with your breath.",
            "Continue for several slow rounds."
        ),
        targetMuscles = listOf("Spine", "Core", "Neck"),
        workSeconds = 45,
        restSeconds = 0,
        tips = listOf("Move with your breath, not faster.", "Keep the motion gentle.")
    )

    val downwardDog = Exercise(
        id = "downward_dog",
        name = "Downward Dog",
        category = WorkoutType.FLEXIBILITY,
        description = "A foundational yoga pose that stretches the hamstrings, calves and shoulders.",
        instructions = listOf(
            "Start on all fours, hands slightly ahead of your shoulders.",
            "Tuck your toes and lift your hips up and back.",
            "Straighten your legs as much as is comfortable.",
            "Press your chest gently toward your thighs to form an inverted V.",
            "Pedal your heels to ease into the stretch."
        ),
        targetMuscles = listOf("Hamstrings", "Calves", "Shoulders", "Back"),
        workSeconds = 45,
        restSeconds = 0,
        tips = listOf("Bend your knees if your hamstrings are tight.", "Spread your fingers for stability.")
    )

    val worldsGreatest = Exercise(
        id = "worlds_greatest_stretch",
        name = "World's Greatest Stretch",
        category = WorkoutType.FLEXIBILITY,
        description = "A multi-joint mobility move that opens the hips, thoracic spine and hamstrings in one flow.",
        instructions = listOf(
            "Step your right foot forward into a deep lunge.",
            "Place your left hand on the floor beside your right foot.",
            "Rotate your torso and reach your right arm up toward the ceiling.",
            "Hold for a breath, then return your hand to the floor.",
            "Switch sides and repeat."
        ),
        targetMuscles = listOf("Hips", "Thoracic spine", "Hamstrings"),
        sets = 2,
        reps = "5 reps per side",
        restSeconds = 0,
        tips = listOf("Move slowly and breathe into each position.", "Keep your front knee over your ankle.")
    )

    val hamstringStretch = Exercise(
        id = "hamstring_stretch",
        name = "Seated Hamstring Stretch",
        category = WorkoutType.FLEXIBILITY,
        description = "A gentle static stretch to lengthen tight hamstrings after training.",
        instructions = listOf(
            "Sit on the floor with both legs extended in front of you.",
            "Sit tall and hinge forward from your hips.",
            "Reach your hands toward your toes without rounding your back too much.",
            "Stop when you feel a comfortable stretch in the back of your legs.",
            "Hold and breathe, then relax."
        ),
        targetMuscles = listOf("Hamstrings", "Lower back"),
        workSeconds = 40,
        restSeconds = 0,
        tips = listOf("Never bounce — hold the stretch steadily.", "Bend your knees slightly if needed.")
    )

    val chestOpener = Exercise(
        id = "chest_opener",
        name = "Standing Chest Opener",
        category = WorkoutType.FLEXIBILITY,
        description = "A stretch that counters slouching by opening the chest and front shoulders.",
        instructions = listOf(
            "Stand tall and clasp your hands behind your back.",
            "Straighten your arms and draw your shoulder blades together.",
            "Gently lift your hands away from your back.",
            "Open your chest and lift your gaze slightly.",
            "Hold and breathe deeply, then release."
        ),
        targetMuscles = listOf("Chest", "Front shoulders"),
        workSeconds = 30,
        restSeconds = 0,
        tips = listOf("Keep your neck relaxed.", "Don't force the range — go to a gentle stretch.")
    )

    // ---------------------------------------------------------------------
    // Additional exercises
    // ---------------------------------------------------------------------

    val squatThrust = Exercise(
        id = "squat_thrust",
        name = "Squat Thrust",
        category = WorkoutType.HIIT,
        description = "A burpee without the jump or push-up — squat, kick the legs back to a plank, then return.",
        instructions = listOf(
            "Stand with feet shoulder-width apart.",
            "Squat down and place your hands on the floor.",
            "Jump or step both feet back into a plank.",
            "Jump or step your feet back to your hands.",
            "Stand up tall and repeat."
        ),
        targetMuscles = listOf("Full body", "Core", "Legs"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Keep your back flat in the plank.", "Step instead of jump to lower the impact.")
    )

    val tuckJump = Exercise(
        id = "tuck_jump",
        name = "Tuck Jump",
        category = WorkoutType.HIIT,
        description = "An explosive vertical jump drawing the knees up to the chest, building leg power.",
        instructions = listOf(
            "Stand with feet hip-width apart and knees softly bent.",
            "Swing your arms down, then jump explosively upward.",
            "Drive your knees up toward your chest at the top.",
            "Land softly with bent knees.",
            "Reset and repeat with control."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Calves", "Core"),
        workSeconds = 30,
        restSeconds = 30,
        tips = listOf("Land quietly to absorb impact.", "Skip the knee tuck if it's too intense.")
    )

    val fastFeet = Exercise(
        id = "fast_feet",
        name = "Fast Feet",
        category = WorkoutType.CARDIO,
        description = "Rapid, small running steps on the balls of your feet to spike your heart rate.",
        instructions = listOf(
            "Stand in a slight athletic crouch, knees bent.",
            "Shift your weight onto the balls of your feet.",
            "Run in place as fast as you can with very small, quick steps.",
            "Pump your arms and stay low.",
            "Keep the cadence high for the whole interval."
        ),
        targetMuscles = listOf("Calves", "Quads", "Cardiovascular system"),
        workSeconds = 30,
        restSeconds = 20,
        tips = listOf("Stay light and quick.", "Keep your chest up and core engaged.")
    )

    val shadowBoxing = Exercise(
        id = "shadow_boxing",
        name = "Shadow Boxing",
        category = WorkoutType.CARDIO,
        description = "Throw controlled punches while moving to raise the heart rate and work the upper body.",
        instructions = listOf(
            "Stand with one foot slightly forward and hands up by your chin.",
            "Throw straight punches alternating hands, rotating your torso.",
            "Stay light on your feet, bobbing and moving.",
            "Mix in jabs, crosses and hooks at a steady pace.",
            "Keep your core tight and breathe with each punch."
        ),
        targetMuscles = listOf("Shoulders", "Arms", "Core", "Cardiovascular system"),
        workSeconds = 40,
        restSeconds = 20,
        tips = listOf("Don't lock out your elbows.", "Keep your hands up to protect your face.")
    )

    val diamondPushUp = Exercise(
        id = "diamond_push_up",
        name = "Diamond Push-Up",
        category = WorkoutType.STRENGTH,
        description = "A close-hand push-up that shifts the emphasis onto the triceps.",
        instructions = listOf(
            "Start in a high plank and bring your hands together under your chest.",
            "Touch your index fingers and thumbs to form a diamond shape.",
            "Keep your body in a straight line and brace your core.",
            "Bend your elbows to lower your chest toward your hands.",
            "Press back up to straight arms."
        ),
        targetMuscles = listOf("Triceps", "Chest", "Front shoulders"),
        sets = 3,
        reps = "6–12 reps",
        restSeconds = 60,
        tips = listOf("Drop to your knees to scale it down.", "Keep your elbows fairly close to your body.")
    )

    val bulgarianSplitSquat = Exercise(
        id = "bulgarian_split_squat",
        name = "Bulgarian Split Squat",
        category = WorkoutType.STRENGTH,
        description = "A rear-foot-elevated single-leg squat that builds serious leg and glute strength.",
        instructions = listOf(
            "Stand a couple of feet in front of a chair or bench, facing away.",
            "Place the top of one foot on the bench behind you.",
            "Keep your chest up and your front foot flat.",
            "Bend your front knee to lower straight down.",
            "Drive through your front heel to return to standing, then switch legs."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings"),
        sets = 3,
        reps = "8–12 reps per leg",
        restSeconds = 60,
        tips = listOf("Keep most of your weight on the front leg.", "Hold a wall for balance if needed.")
    )

    val stepUp = Exercise(
        id = "step_up",
        name = "Step-Up",
        category = WorkoutType.STRENGTH,
        description = "Stepping onto a raised surface to build single-leg strength and balance.",
        instructions = listOf(
            "Stand facing a sturdy chair, bench or step.",
            "Place one whole foot on the step.",
            "Drive through that heel to stand all the way up on the step.",
            "Step back down with control under the same leg.",
            "Complete your reps, then switch legs."
        ),
        targetMuscles = listOf("Quads", "Glutes", "Hamstrings"),
        equipment = "Sturdy step or bench",
        sets = 3,
        reps = "10 reps per leg",
        restSeconds = 45,
        tips = listOf("Push through the heel, not the toes.", "Keep your knee tracking over your foot.")
    )

    val vUp = Exercise(
        id = "v_up",
        name = "V-Up",
        category = WorkoutType.CORE,
        description = "A dynamic sit-up that lifts the arms and legs together into a V to work the whole core.",
        instructions = listOf(
            "Lie flat on your back with arms extended overhead and legs straight.",
            "Brace your core.",
            "Simultaneously lift your legs and torso, reaching your hands toward your toes.",
            "Form a V shape, balancing on your hips.",
            "Lower back down with control without letting your feet touch."
        ),
        targetMuscles = listOf("Rectus abdominis", "Hip flexors"),
        sets = 3,
        reps = "10–15 reps",
        restSeconds = 30,
        tips = listOf("Bend your knees to make it easier.", "Move smoothly rather than using momentum.")
    )

    val reverseCrunch = Exercise(
        id = "reverse_crunch",
        name = "Reverse Crunch",
        category = WorkoutType.CORE,
        description = "A crunch variation that curls the hips toward the chest to target the lower abs.",
        instructions = listOf(
            "Lie on your back with hands by your sides and knees bent over your hips.",
            "Press your lower back into the floor.",
            "Curl your hips upward, bringing your knees toward your chest.",
            "Lift your hips a few inches off the floor.",
            "Lower with control and repeat."
        ),
        targetMuscles = listOf("Lower abs", "Core"),
        sets = 3,
        reps = "12–15 reps",
        restSeconds = 30,
        tips = listOf("Use your abs, not momentum, to lift.", "Keep the movement small and controlled.")
    )

    val plankShoulderTap = Exercise(
        id = "plank_shoulder_tap",
        name = "Plank Shoulder Tap",
        category = WorkoutType.CORE,
        description = "A high-plank hold with alternating shoulder taps that challenges anti-rotation core strength.",
        instructions = listOf(
            "Start in a high plank with hands under shoulders, feet wide for stability.",
            "Brace your core and squeeze your glutes.",
            "Lift one hand to tap the opposite shoulder.",
            "Return it and tap with the other hand.",
            "Keep your hips as still as possible throughout."
        ),
        targetMuscles = listOf("Core", "Shoulders", "Obliques"),
        workSeconds = 30,
        restSeconds = 20,
        tips = listOf("Widen your feet to reduce hip rocking.", "Move slowly and stay tight.")
    )

    val birdDog = Exercise(
        id = "bird_dog",
        name = "Bird Dog",
        category = WorkoutType.CORE,
        description = "An all-fours exercise extending opposite arm and leg to build core stability and balance.",
        instructions = listOf(
            "Start on all fours with hands under shoulders and knees under hips.",
            "Brace your core and keep your back flat.",
            "Extend your right arm forward and your left leg back.",
            "Reach until they're level with your torso, then pause.",
            "Return and repeat on the opposite side."
        ),
        targetMuscles = listOf("Core", "Lower back", "Glutes"),
        sets = 3,
        reps = "10 reps per side",
        restSeconds = 30,
        tips = listOf("Keep your hips square to the floor.", "Move slowly and don't arch your back.")
    )

    val armCircles = Exercise(
        id = "arm_circles",
        name = "Arm Circles",
        category = WorkoutType.FLEXIBILITY,
        description = "Controlled circling of the arms to warm up and mobilise the shoulders.",
        instructions = listOf(
            "Stand tall and extend both arms out to the sides at shoulder height.",
            "Make small circles forward, gradually growing larger.",
            "After a few seconds, reverse direction.",
            "Keep your shoulders relaxed and core gently braced."
        ),
        targetMuscles = listOf("Shoulders"),
        workSeconds = 30,
        restSeconds = 0,
        tips = listOf("Start small and build the size gradually.", "Breathe steadily throughout.")
    )

    val cobraStretch = Exercise(
        id = "cobra_stretch",
        name = "Cobra Stretch",
        category = WorkoutType.FLEXIBILITY,
        description = "A gentle backbend that stretches the abs and the front of the body.",
        instructions = listOf(
            "Lie face down with hands under your shoulders.",
            "Press through your hands to lift your chest off the floor.",
            "Keep your hips and legs on the ground.",
            "Open your chest and lengthen your neck.",
            "Hold and breathe, then lower down slowly."
        ),
        targetMuscles = listOf("Abs", "Lower back", "Chest"),
        workSeconds = 40,
        restSeconds = 0,
        tips = listOf("Only lift as far as is comfortable.", "Keep your shoulders away from your ears.")
    )

    val lungeWithTwist = Exercise(
        id = "lunge_with_twist",
        name = "Lunge with Twist",
        category = WorkoutType.FLEXIBILITY,
        description = "A dynamic mobility move combining a lunge with a torso rotation to open the hips and spine.",
        instructions = listOf(
            "Step forward into a lunge with your right foot.",
            "Keep your back tall and your front knee over your ankle.",
            "Rotate your torso to the right over your front leg.",
            "Return to center, push back to standing, and switch sides.",
            "Move smoothly with your breath."
        ),
        targetMuscles = listOf("Hips", "Thoracic spine", "Glutes"),
        sets = 2,
        reps = "6 reps per side",
        restSeconds = 0,
        tips = listOf("Rotate from the mid-back, not the lower back.", "Keep the front heel planted.")
    )
}
