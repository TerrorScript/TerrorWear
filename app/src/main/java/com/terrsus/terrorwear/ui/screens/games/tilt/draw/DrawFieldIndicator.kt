package com.terrsus.terrorwear.ui.screens.games.tilt.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.domain.games.tilt.model.Ball
import com.terrsus.terrorwear.domain.games.tilt.model.Obstacle
import com.terrsus.terrorwear.domain.games.tilt.model.ObstaclePhase

/**
 * Draws faint directional indicators pointing from the ball toward obstacles.
 */
fun DrawScope.drawFieldIndicators(ball: Ball, obstacles: List<Obstacle>) {
    val ballPos = Offset(ball.position.x, ball.position.y)

    obstacles.forEach { o ->
        if (o.phase == ObstaclePhase.ACTIVE_IN_FIELD) {
            val obsPos = Offset(o.position.x, o.position.y)

            drawLine(
                color = Color.White.copy(alpha = 0.2f),
                start = ballPos,
                end = obsPos,
                strokeWidth = 2f
            )
        }
    }
}
