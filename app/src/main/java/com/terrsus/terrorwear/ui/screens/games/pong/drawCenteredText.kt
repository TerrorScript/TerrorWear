package com.terrsus.terrorwear.ui.screens.games.pong

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

/**
 * Draws centered text on the canvas.
 */
fun DrawScope.drawCenteredText(text: String) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            text,
            size.width / 2,
            size.height / 2,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}