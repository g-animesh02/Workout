# FitTrack — Workout Schedule & Calorie Tracker

A **native Android app** (Kotlin + Jetpack Compose) for following a weekly
training plan and tracking the food you eat. It is **100% free, offline, and
ad-free** — no account, no subscriptions, no network calls, no paid APIs.

## Features

### 🗓️ Weekly training schedule
A balanced default plan, ready to use out of the box:

| Day | Workout | Focus |
|-----|---------|-------|
| Monday | **HIIT Blast** | Fat burn · conditioning |
| Tuesday | **Upper Body Strength** | Chest · shoulders · arms |
| Wednesday | **Lower Body Strength** | Quads · glutes · hamstrings |
| Thursday | **Core & Abs** | Abs · obliques · stability |
| Friday | **Full Body Burn** | Strength + cardio finisher |
| Saturday | **Mobility & Stretch** | Recovery · flexibility |
| Sunday | **Rest Day** | Active recovery |

### 💪 Exercise guides
Every exercise includes:
- **What it is** — a plain-language description.
- **How to do it** — numbered, step-by-step instructions.
- **Form tips** — cues and easier/harder scaling options.
- **Target muscles** and recommended sets/reps or work/rest timing.

35+ bodyweight exercises across HIIT, strength, core, cardio and mobility — no
equipment required.

### 🍎 Food & calorie tracking
- Log meals with calories and optional protein / carbs / fat macros.
- **Quick-add** from a built-in catalog of common foods (one tap).
- Daily calorie ring with goal progress and remaining calories.
- Browse any day; set your own daily calorie goal.

### 🧩 Home-screen widgets
Two resizable widgets for at-a-glance access:
- **Calories widget** — today's total vs. goal, plus a one-tap **“+ Add food”**
  button that opens a lightweight quick-add screen without launching the full app.
- **Schedule widget** — today's scheduled workout; tap to open the plan.

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3 (dynamic color on Android 12+)
- **Architecture:** MVVM with `ViewModel` + Kotlin `Flow`
- **Persistence:** Room (food log) + DataStore (settings) — fully local
- **Widgets:** Jetpack Glance
- **Min SDK:** 26 (Android 8.0) · **Target/Compile SDK:** 35

## Project structure

```
app/src/main/java/com/fittrack/app/
├── FitTrackApp.kt            # Application + simple DI container
├── MainActivity.kt           # Compose host, nav graph, bottom nav
├── QuickAddActivity.kt       # Lightweight add-food screen for the widget
├── data/
│   ├── model/                # Exercise, Workout, WorkoutDay
│   ├── db/                   # Room database, DAO, FoodEntry entity
│   ├── repository/           # Food + Settings repositories
│   └── seed/                 # ExerciseLibrary, weekly plan, food catalog
├── ui/
│   ├── theme/                # Colors, typography, Material 3 theme
│   ├── navigation/           # Routes
│   └── screens/              # schedule, workout, exercise, food
└── widget/                   # Glance widgets + updater
```

## Build & run

```bash
./gradlew assembleDebug      # build a debug APK
./gradlew installDebug       # install on a connected device/emulator
./gradlew test               # run JVM unit tests
```

Open the project in **Android Studio** (Ladybug or newer), let it sync, and run
the `app` configuration on a device or emulator running Android 8.0+.

> The Gradle wrapper is committed. The first build downloads the Android Gradle
> Plugin and AndroidX dependencies, so it requires internet access **once**; the
> app itself never uses the network at runtime.

## License

Free to use. Built with offline, open tooling — no paid services required.
