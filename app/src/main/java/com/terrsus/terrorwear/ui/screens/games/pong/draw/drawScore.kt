package com.terrsus.terrorwear.ui.screens.games.pong.draw

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.terrsus.terrorwear.domain.games.pong.model.GameState

/**
 * Draws the score.
 */
fun DrawScope.drawScore(state: GameState) {
    drawContext.canvas.nativeCanvas.apply {
        drawText(
            "${state.playerScore} : ${state.enemyScore}",
            size.width / 2,
            size.height * 0.2f,
            Paint().apply {
                color = Color.WHITE
                textSize = 32f
                textAlign = Paint.Align.CENTER
            }
        )
    }
}