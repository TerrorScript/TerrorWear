package com.terrsus.terrorwear.modules.games.tilt.ui.draw

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

/**
 * Draws a subtle CRT-style vignette + scanlines + bloom.
 */
fun DrawScope.drawCrtOverlay() {
    val canvas = drawContext.canvas.nativeCanvas

    // Vignette
    val vignettePaint = Paint().apply {
        isAntiAlias = true
        shader = RadialGradient(
            size.width / 2f,
            size.height / 2f,
            size.minDimension / 1.1f,
            intArrayOf(Color.TRANSPARENT, Color.argb(120, 0, 0, 0)),
            floatArrayOf(0.7f, 1f),
            Shader.TileMode.CLAMP
        )
    }
    canvas.drawRect(0f, 0f, size.width, size.height, vignettePaint)

    // Scanlines
    val scanPaint = Paint().apply {
        color = Color.argb(70, 0, 0, 0)
        strokeWidth = 3f
    }
    var y = 0f
    while (y < size.height) {
        canvas.drawLine(0f, y, size.width, y, scanPaint)
        y += 6f
    }

    // Bloom
    val glowPaint = Paint().apply {
        color = Color.argb(20, 255, 255, 255)
        maskFilter = BlurMaskFilter(12f, BlurMaskFilter.Blur.NORMAL)
    }
    canvas.drawRect(0f, 0f, size.width, size.height, glowPaint)
}
