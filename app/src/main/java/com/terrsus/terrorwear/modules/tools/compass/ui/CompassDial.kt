package com.terrsus.terrorwear.modules.tools.compass.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Draws the compass dial, tick marks, needle, and direction ring.
 */
@Composable
fun CompassDial(yaw: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize(0.9f)
    ) {
        val center = size / 2f
        val radius = size.minDimension / 2.2f

        // Outer circle
        drawCircle(
            color = Color(0xFF444444),
            radius = radius,
            style = Stroke(width = 4f)
        )

        // Tick marks every 30°
        for (deg in 0 until 360 step 30) {
            val angleRad = Math.toRadians(deg.toDouble())
            val inner = radius * 0.85f
            val outer = radius

            val start = Offset(
                x = center.width + inner * kotlin.math.sin(angleRad).toFloat(),
                y = center.height - inner * kotlin.math.cos(angleRad).toFloat()
            )
            val end = Offset(
                x = center.width + outer * kotlin.math.sin(angleRad).toFloat(),
                y = center.height - outer * kotlin.math.cos(angleRad).toFloat()
            )

            drawLine(
                color = if (deg % 90 == 0) Color.Red else Color.White,
                start = start,
                end = end,
                strokeWidth = if (deg % 90 == 0) 6f else 3f
            )
        }

        val angle = Math.toRadians((-yaw - 90).toDouble())

        // Needle
        drawLine(
            color = Color.Red,
            start = Offset(
                x = center.width,
                y = center.height
            ),
            end = Offset(
                x = center.width + radius * 0.75f * cos(angle).toFloat(),
                y = center.height + radius * 0.75f * sin(angle).toFloat()
            ),
            strokeWidth = 8f
        )

        // Heading text
        drawContext.canvas.nativeCanvas.apply {
            val heading = "${yaw.roundToInt()}°"
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 48f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }
            drawText(
                heading,
                center.width,
                center.height + 20f,
                paint
            )
        }

        // Cardinal direction label at ~60% down
        val dirs = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val index = ((yaw) / 45f).toInt().mod(8)
        val cardinal = dirs[index]

        drawContext.canvas.nativeCanvas.apply {
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 42f
                textAlign = android.graphics.Paint.Align.CENTER
                isFakeBoldText = true
            }

            // 60% down from center toward bottom of dial
            val y = center.height + (radius * 0.60f)

            drawText(
                cardinal,
                center.width,
                y,
                paint
            )
        }
    }
}