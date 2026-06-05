package com.terrsus.terrorwear.ui.screens.games.pong.draw

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.terrsus.terrorwear.domain.games.pong.model.GameState

/**
 * Draws the game timer.
 */
fun DrawScope.drawTimer(state: GameState) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            formatTime(state.totalTime),
            size.width / 2,
            size.height - 20f,
            Paint().apply {
                color = Color.WHITE
                textSize = 28f
                textAlign = Paint.Align.CENTER
            }
        )
    }
}

/**
 * Formats seconds as MM:SS.
 *
 * @param seconds the time, in seconds, to format.
 */
private fun formatTime(seconds: Float): String {
    val total = seconds.toInt()
    val m = total / 60
    val s = total % 60
    return "%02d:%02d".format(m, s)
}