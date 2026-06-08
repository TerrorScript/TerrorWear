package com.terrsus.terrorwear.modules.tools.cameraremote.ui

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CompactButton
import androidx.wear.compose.material.Icon

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    iconRes: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(50.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null
        )
    }
}