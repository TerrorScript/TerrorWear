package com.terrsus.terrorwear.modules.games.tilt.ui.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.features.games.tilt.domain.model.Ball
import com.terrsus.terrorwear.features.games.tilt.domain.model.Obstacle
import com.terrsus.terrorwear.features.games.tilt.domain.model.ObstaclePhase

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
