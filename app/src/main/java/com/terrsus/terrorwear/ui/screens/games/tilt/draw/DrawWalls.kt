package com.terrsus.terrorwear.ui.screens.games.tilt.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.domain.games.tilt.model.Wall

/**
 * Draws temporary or static walls.
 */
fun DrawScope.drawWalls(walls: List<Wall>) {
    walls.forEach { w ->
        drawLine(
            color = Color.Cyan,
            start = Offset(w.start.x, w.start.y),
            end = Offset(w.end.x, w.end.y),
            strokeWidth = 6f
        )
    }
}
