package com.terrsus.terrorwear.modules.tools.cameraremote.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.R

@Composable
fun ShutterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(80.dp)
    ) {
        Icon(
            painter = painterResource(CameraIcons.Camera),
            contentDescription = "Shutter"
        )
    }
}