package com.terrsus.terrorwear.modules.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.modules.settings.viewmodel.SettingsViewModel

/**
 * Wear OS–native settings screen using ScalingLazyColumn and ToggleChip.
 */
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val telemetryEnabled = viewModel.telemetryEnabled.collectAsState()
    val haptics = LocalHapticFeedback.current

    val scrollState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = scrollState)
        },
    ) {
        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {

            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )
            }

            item {
                ToggleChip(
                    checked = telemetryEnabled.value,
                    onCheckedChange = { enabled ->
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.setTelemetryEnabled(enabled)
                    },
                    label = {
                        Text("Enable Telemetry")
                    },
                    toggleControl = {
                        Switch(checked = telemetryEnabled.value)
                    }
                )
            }
        }
    }
}