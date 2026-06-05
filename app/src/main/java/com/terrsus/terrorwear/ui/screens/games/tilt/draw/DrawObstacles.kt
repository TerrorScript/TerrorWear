package com.terrsus.terrorwear.ui.screens.games.tilt.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.domain.games.tilt.model.Obstacle
import com.terrsus.terrorwear.domain.games.tilt.model.ObstaclePhase

/**
 * Draws active obstacles (not border indicators).
 */
fun DrawScope.drawObstacles(obstacles: List<Obstacle>) {
    obstacles.forEach { o ->
        if (o.phase == ObstaclePhase.ACTIVE_IN_FIELD) {
            drawCircle(
                color = Color.Red,
                radius = 12f,
                center = Offset(o.position.x, o.position.y)
            )
        }
    }
}
