package com.fittrack.app.ui.screens.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fittrack.app.data.model.Exercise
import com.fittrack.app.ui.IconBadge
import com.fittrack.app.ui.Pill
import com.fittrack.app.ui.color
import com.fittrack.app.ui.icon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = exercise.category.color()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(exercise.name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Header(exercise, accent) }
            item { VideoDemoButton(exercise, accent) }
            item { WhatItIs(exercise) }
            item { HowToDoIt(exercise, accent) }
            if (exercise.tips.isNotEmpty()) {
                item { Tips(exercise) }
            }
            item { TargetMuscles(exercise, accent) }
        }
    }
}

@Composable
private fun VideoDemoButton(exercise: Exercise, accent: Color) {
    val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
    val query = java.net.URLEncoder.encode("how to do ${exercise.name} exercise", "UTF-8")
    androidx.compose.material3.Button(
        onClick = { uriHandler.openUri("https://www.youtube.com/results?search_query=$query") },
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = accent)
    ) {
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text("Watch video demo")
    }
}

@Composable
private fun Header(exercise: Exercise, accent: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(icon = exercise.category.icon(), color = accent, sizeDp = 52)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(exercise.name, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        exercise.category.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = accent,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            if (exercise.dosage.isNotBlank() || exercise.equipment.isNotBlank()) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (exercise.dosage.isNotBlank()) {
                        StatItem(Icons.Filled.Timer, exercise.dosage)
                    }
                    StatItem(Icons.Filled.FitnessCenter, exercise.equipment)
                }
            }
        }
    }
}

@Composable
private fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun WhatItIs(exercise: Exercise) {
    SectionCard(title = "What it is") {
        Text(exercise.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun HowToDoIt(exercise: Exercise, accent: Color) {
    SectionCard(title = "How to do it") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            exercise.instructions.forEachIndexed { index, step ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(accent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(
                        step,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Tips(exercise: Exercise) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Form tips",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.height(8.dp))
            exercise.tips.forEach { tip ->
                Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                    Text("•  ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                    Text(tip, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TargetMuscles(exercise: Exercise, accent: Color) {
    SectionCard(title = "Target muscles") {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            exercise.targetMuscles.forEach { muscle ->
                Box(Modifier.padding(bottom = 8.dp)) {
                    Pill(text = muscle, color = accent)
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}
