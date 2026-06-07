package com.terrsus.terrorwear.modules.games.pong.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.terrsus.terrorwear.features.games.pong.domain.model.GameState

/**
 * Draws both paddles.
 */
fun DrawScope.drawPaddles(state: GameState) {
    drawRect(
        color = Color.Blue,
        topLeft = Offset(state.playerPaddle.position.x, state.playerPaddle.position.y),
        size = Size(state.playerPaddle.width, state.playerPaddle.height)
    )
    drawRect(
        color = Color.Red,
        topLeft = Offset(state.enemyPaddle.position.x, state.enemyPaddle.position.y),
        size = Size(state.enemyPaddle.width, state.enemyPaddle.height)
    )
}