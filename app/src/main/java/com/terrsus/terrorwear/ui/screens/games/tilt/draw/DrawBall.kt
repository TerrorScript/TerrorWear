package com.terrsus.terrorwear.ui.screens.games.tilt.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.terrsus.terrorwear.domain.games.tilt.model.Ball

/**
 * Draws the player ball.
 */
fun DrawScope.drawBall(ball: Ball) {
    drawCircle(
        color = Color.White,
        radius = ball.radius,
        center = androidx.compose.ui.geometry.Offset(ball.position.x, ball.position.y)
    )
}
