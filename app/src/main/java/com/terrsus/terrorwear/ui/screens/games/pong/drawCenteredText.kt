package com.terrsus.terrorwear.ui.screens.games.pong

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

/**
 * Draws centered text.
 *
 * @param text The text to draw.
 * @param y Vertical position.
 */
fun DrawScope.drawCenteredText(
    text: String,
    x: Float = size.width / 2f,
    y: Float = size.height / 2f
) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            text,
            x,
            y,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}