package com.terrsus.terrorwear.modules.tools.cameraremote.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.modules.tools.cameraremote.ui.SmallButton
import com.terrsus.terrorwear.modules.tools.cameraremote.viewmodel.CameraRemoteViewModel
import kotlinx.coroutines.delay
import com.terrsus.terrorwear.R

@Composable
fun CameraRemoteScreen(
    viewModel: CameraRemoteViewModel
) {
    val haptics = LocalHapticFeedback.current

    val flashEnabled by viewModel.flashEnabled.collectAsState()
    val zoom by viewModel.zoom.collectAsState()
    val status by viewModel.status.collectAsState()

    var lastStatus by remember { mutableStateOf("") }
    if (status.isNotEmpty()) lastStatus = status

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    34.dp
                ),
            contentAlignment = Alignment.Center
        ) {

            // --- Shutter Button ---
            ShutterButton(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.takePhoto()
                }
            )

            // --- Flash Toggle ---
            SmallButton(
                modifier = Modifier.align(Alignment.TopStart),
                iconRes = if (flashEnabled) CameraIcons.FlashOn else CameraIcons.FlashOff,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.toggleFlash()
                }
            )

            // --- Switch Camera ---
            SmallButton(
                modifier = Modifier.align(Alignment.TopEnd),
                iconRes = CameraIcons.SwitchCamera,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.switchCamera()
                }
            )

            // --- Zoom Slider ---
            ZoomControl(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .height(32.dp),
                zoom = zoom,
                onZoomChange = { viewModel.setZoom(it) }
            )

            // --- Status Popup ---
            AnimatedVisibility(
                visible = status.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                StatusPopup(lastStatus)
            }

            LaunchedEffect(status) {
                if (status.isNotEmpty()) {
                    delay(1200)
                    viewModel.clearStatus()
                }
            }
        }
    }
}
