package com.terrsus.terrorwear.modules.games.tilt.ui.draw

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.pow

/**
 * Draws a curved grid simulating CRT barrel distortion.
 */
fun DrawScope.drawScreenWarpGrid(intensity: Float = 0.12f, step: Float = 20f) {
    val canvas = drawContext.canvas.nativeCanvas

    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cy = h / 2f

    val paint = Paint().apply {
        color = Color.argb(40, 255, 255, 255)
        strokeWidth = 1f
    }

    fun warp(v: Float, center: Float): Float {
        val norm = (v - center) / center
        val warped = norm * (1 + intensity * norm.pow(2))
        return center + warped * center
    }

    var x = 0f
    while (x <= w) {
        val x0 = warp(x, cx)
        canvas.drawLine(x0, 0f, x0, h, paint)
        x += step
    }

    var y = 0f
    while (y <= h) {
        val y0 = warp(y, cy)
        canvas.drawLine(0f, y0, w, y0, paint)
        y += step
    }
}
