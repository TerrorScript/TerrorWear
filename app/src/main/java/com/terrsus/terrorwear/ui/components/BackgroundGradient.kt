package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Full‑screen radial gradient background for Wear OS screens.
 *
 * This draws directly into a Canvas, which gives access to `size`
 * without needing any imports or extra modifiers.
 *
 * @receiver Composable container for the gradient.
 */
@Composable
fun BackgroundGradient(
    colors: List<Color> = listOf(Color(0xFF101010), Color.Black),
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = colors,
                center = center,
                radius = size.minDimension * 0.9f
            )
        )
    }
}