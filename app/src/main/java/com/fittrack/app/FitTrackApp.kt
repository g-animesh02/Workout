package com.fittrack.app

import android.app.Application
import com.fittrack.app.data.db.AppDatabase
import com.fittrack.app.data.repository.FoodRepository
import com.fittrack.app.data.repository.SettingsRepository

/**
 * Application class acting as a tiny manual dependency container. The app is
 * fully offline and free, so there are no API keys or remote services to wire.
 */
class FitTrackApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.get(this) }
    val foodRepository: FoodRepository by lazy { FoodRepository(database.foodDao()) }
    val settingsRepository: SettingsRepository by lazy { SettingsRepository(this) }

    companion object {
        lateinit var instance: FitTrackApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
