package com.terrsus.terrorwear.ui.screens.games.tilt.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.domain.games.tilt.model.Obstacle
import com.terrsus.terrorwear.domain.games.tilt.model.ObstaclePhase

/**
 * Draws warning indicators along the border for incoming obstacles.
 */
fun DrawScope.drawBorderIndicators(obstacles: List<Obstacle>) {
    obstacles.forEach { o ->
        if (o.phase == ObstaclePhase.WARNING_BORDER) {
            drawCircle(
                color = Color.Yellow,
                radius = 8f,
                center = Offset(o.position.x, o.position.y)
            )
        }
    }
}
