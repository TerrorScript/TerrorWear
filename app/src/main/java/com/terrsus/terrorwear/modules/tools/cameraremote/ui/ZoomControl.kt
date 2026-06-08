package com.terrsus.terrorwear.modules.tools.cameraremote.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.InlineSlider

@Composable
fun ZoomControl(
    modifier: Modifier = Modifier,
    zoom: Float,
    onZoomChange: (Float) -> Unit
) {
    InlineSlider(
        value = zoom,
        onValueChange = onZoomChange,
        valueRange = 1f..4f,
        steps = 3,
        modifier = modifier,
        decreaseIcon = {
            Icon(
                painter = painterResource(CameraIcons.ZoomOut),
                contentDescription = "Zoom out"
            )
        },
        increaseIcon = {
            Icon(
                painter = painterResource(CameraIcons.ZoomIn),
                contentDescription = "Zoom in"
            )
        }
    )
}