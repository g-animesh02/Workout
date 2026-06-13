package com.fittrack.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.fittrack.app.data.model.WorkoutType
import com.fittrack.app.ui.theme.CardioColor
import com.fittrack.app.ui.theme.CoreColor
import com.fittrack.app.ui.theme.FlexColor
import com.fittrack.app.ui.theme.FullBodyColor
import com.fittrack.app.ui.theme.HiitColor
import com.fittrack.app.ui.theme.RestColor
import com.fittrack.app.ui.theme.StrengthColor

fun WorkoutType.color(): Color = when (this) {
    WorkoutType.HIIT -> HiitColor
    WorkoutType.STRENGTH -> StrengthColor
    WorkoutType.CARDIO -> CardioColor
    WorkoutType.CORE -> CoreColor
    WorkoutType.FLEXIBILITY -> FlexColor
    WorkoutType.FULL_BODY -> FullBodyColor
    WorkoutType.REST -> RestColor
}

fun WorkoutType.icon(): ImageVector = when (this) {
    WorkoutType.HIIT -> Icons.Filled.Whatshot
    WorkoutType.STRENGTH -> Icons.Filled.FitnessCenter
    WorkoutType.CARDIO -> Icons.Filled.DirectionsRun
    WorkoutType.CORE -> Icons.Filled.Bolt
    WorkoutType.FLEXIBILITY -> Icons.Filled.SelfImprovement
    WorkoutType.FULL_BODY -> Icons.Filled.AccessibilityNew
    WorkoutType.REST -> Icons.Filled.Spa
}
