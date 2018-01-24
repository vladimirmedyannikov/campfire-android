package com.pandulapeter.campfire

import android.app.Application
import com.pandulapeter.campfire.injection.*
import org.koin.android.ext.android.startKoin

/**
 * Custom Application class for initializing dependency injection and crash reporting.
 *
 * TODO: Introduce Firebase crash reporting.
 */
class CampfireApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(integrationModule, networkingModule, repositoryModule, storageModule, bridgeModule))
    }
}