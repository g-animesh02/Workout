package com.fittrack.app.ui.screens.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.fittrack.app.data.db.entity.MealType
import com.fittrack.app.data.seed.FoodCatalog
import com.fittrack.app.data.seed.FoodCategory

/**
 * Dialog for logging food. Offers Indian and common food presets that auto-fill
 * the form (so calories and macros update instantly), with a live detailed
 * breakup of the entry. Fully offline.
 */
@Composable
fun AddFoodDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, calories: Int, protein: Int, carbs: Int, fat: Int, meal: MealType) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var meal by remember { mutableStateOf(MealType.SNACK) }
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf<FoodCategory?>(FoodCategory.INDIAN) }

    val presets = remember(search, category) { FoodCatalog.search(search, category) }

    val cal = calories.toIntOrNull() ?: 0
    val p = protein.toIntOrNull() ?: 0
    val c = carbs.toIntOrNull() ?: 0
    val f = fat.toIntOrNull() ?: 0

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.padding(horizontal = 20.dp),
        title = { Text("Add food") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                // Meal type
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    MealType.entries.forEach { type ->
                        FilterChip(
                            selected = meal == type,
                            onClick = { meal = type },
                            label = { Text(type.label, style = MaterialTheme.typography.labelMedium) }
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))

                // Live detailed breakup
                BreakupBar(calories = cal, protein = p, carbs = c, fat = f)
                Spacer(Modifier.height(10.dp))

                // Editable fields
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Food name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberField(calories, { calories = it }, "Calories", Modifier.weight(1f))
                    NumberField(protein, { protein = it }, "Protein g", Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NumberField(carbs, { carbs = it }, "Carbs g", Modifier.weight(1f))
                    NumberField(fat, { fat = it }, "Fat g", Modifier.weight(1f))
                }

                Spacer(Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(Modifier.height(10.dp))

                Text("Choose a food", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Category filter
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryChip("All", category == null) { category = null }
                    CategoryChip("Indian", category == FoodCategory.INDIAN) { category = FoodCategory.INDIAN }
                    CategoryChip("Common", category == FoodCategory.COMMON) { category = FoodCategory.COMMON }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    label = { Text("Search foods") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.heightIn(max = 220.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(presets, key = { it.name }) { preset ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Auto-fill the form — calories/macros update instantly.
                                    name = preset.name
                                    calories = preset.calories.toString()
                                    protein = preset.protein.toString()
                                    carbs = preset.carbs.toString()
                                    fat = preset.fat.toString()
                                }
                        ) {
                            Row(
                                Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(preset.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        preset.servingLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        "P ${preset.protein}g · C ${preset.carbs}g · F ${preset.fat}g",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    "${preset.calories} kcal",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = cal > 0,
                onClick = {
                    onAdd(name.ifBlank { "Food" }, cal, p, c, f, meal)
                }
            ) { Text("Add") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun BreakupBar(calories: Int, protein: Int, carbs: Int, fat: Int) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text("This entry", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "$calories kcal",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            MacroPill("P", protein)
            Spacer(Modifier.width(8.dp))
            MacroPill("C", carbs)
            Spacer(Modifier.width(8.dp))
            MacroPill("F", fat)
        }
    }
}

@Composable
private fun MacroPill(label: String, grams: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("${grams}g", style = MaterialTheme.typography.titleMedium)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) }
    )
}

@Composable
private fun NumberField(value: String, onChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onChange(input.filter { it.isDigit() }.take(5)) },
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

@Composable
fun EditGoalDialog(current: Int, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var goal by remember { mutableStateOf(current.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daily calorie goal") },
        text = {
            Column {
                Text(
                    "Set the calorie target shown on your tracker and widget.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = goal,
                    onValueChange = { goal = it.filter { ch -> ch.isDigit() }.take(4) },
                    label = { Text("Calories (kcal)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = (goal.toIntOrNull() ?: 0) >= 800,
                onClick = { onSave(goal.toIntOrNull() ?: 2000) }
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
