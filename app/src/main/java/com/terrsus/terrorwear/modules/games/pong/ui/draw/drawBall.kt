package com.terrsus.terrorwear.modules.games.pong.ui.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.terrsus.terrorwear.features.games.pong.domain.model.GameState

/**
 * Draws the ball.
 */
fun DrawScope.drawBall(state: GameState) {
    drawCircle(
        color = Color.White,
        radius = state.ball.radius,
        center = Offset(state.ball.position.x, state.ball.position.y)
    )
}