package com.terrsus.terrorwear.modules.imu.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/** Helper to draw a centered arc at a given radius */
fun DrawScope.drawArcAtRadius(
    color: Color,
    sweep: Float,
    radius: Float,
    stroke: Float = 0f,
    alpha: Float = 1f
) {
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(
            x = size.width / 2f - radius,
            y = size.height / 2f - radius
        ),
        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
        style = Stroke(width = stroke),
        alpha = alpha
    )
}