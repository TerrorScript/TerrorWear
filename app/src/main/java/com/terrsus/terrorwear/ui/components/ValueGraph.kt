package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * Simple line graph showing yaw changes over time.
 */
@Composable
fun ValueGraph(
    values: List<Float>,
    color: Color,
    alpha: Float = 1f
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (values.isEmpty()) return@Canvas

        val halfHeight = size.height * 0.5f

        val max = 360f
        val stepX = size.width / (values.size - 1).coerceAtLeast(1)
        var last = Offset(
            x = 0f,
            y = halfHeight - (values.first() / max) * size.height
        )

        values.forEachIndexed { i, v ->
            val x = i * stepX
            val y = halfHeight - (v / max) * size.height
            val point = Offset(x, y)
            drawLine(
                color = color,
                start = last,
                end = point,
                strokeWidth = 2f,
                alpha = alpha
            )
            last = point
        }
    }
}