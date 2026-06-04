package com.terrsus.terrorwear.ui.screens.games.pong

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
            80f,
            android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }
}