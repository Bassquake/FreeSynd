package com.bassquake.freesynd

import android.app.Application
import android.content.Context
// Import the function directly from the utils package
import com.bassquake.freesynd.utils.copyAllAssetsToInternalStorage

class InstallAssets : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("app_setup", Context.MODE_PRIVATE)

        if (prefs.getBoolean("is_first_run", true)) {
            // This runs synchronously and blocks the app launch until finished
            copyAllAssetsToInternalStorage(applicationContext)

            prefs.edit().putBoolean("is_first_run", false).apply()
        }
    }
}