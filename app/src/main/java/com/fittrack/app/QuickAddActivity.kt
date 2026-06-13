package com.fittrack.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.fittrack.app.data.db.entity.FoodEntry
import com.fittrack.app.ui.screens.food.AddFoodDialog
import com.fittrack.app.ui.theme.FitTrackTheme
import com.fittrack.app.widget.WidgetUpdater
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * A lightweight, transparent activity launched from the home-screen Food widget.
 * It shows only the "Add food" dialog so the user can log a meal without opening
 * the full app, then refreshes the widgets and closes.
 */
class QuickAddActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = applicationContext as FitTrackApp

        setContent {
            FitTrackTheme {
                AddFoodDialog(
                    onDismiss = { finish() },
                    onAdd = { name, calories, protein, carbs, fat, meal ->
                        lifecycleScope.launch {
                            app.foodRepository.addEntry(
                                FoodEntry(
                                    name = name,
                                    calories = calories,
                                    protein = protein,
                                    carbs = carbs,
                                    fat = fat,
                                    mealType = meal,
                                    epochDay = LocalDate.now().toEpochDay()
                                )
                            )
                            WidgetUpdater(app).refreshAll()
                            Toast.makeText(this@QuickAddActivity, "Added $name", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                )
            }
        }
    }
}
