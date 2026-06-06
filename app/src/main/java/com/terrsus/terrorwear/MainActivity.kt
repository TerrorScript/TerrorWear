package com.terrsus.terrorwear

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices

/**
 * Main entry point for the TerrorWear application.
 *
 * This activity initializes the global [AppContainer] with the application
 * context and sets the root composable for the Wear OS UI.
 *
 * The splash screen is installed before the activity content is drawn.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show splash screen until first frame is ready
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Initialize dependency container with application context
        AppContainer.init(this)

        // Use a simple system theme for Wear OS
        setTheme(android.R.style.Theme_DeviceDefault)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Set the root composable for the app
        setContent {
            TerrorWearApp()
        }
    }
}
