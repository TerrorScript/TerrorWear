package com.terrsus.terrorwear.modules.games.tilt.ui.draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import com.terrsus.terrorwear.features.games.tilt.domain.model.Powerup
import com.terrsus.terrorwear.features.games.tilt.domain.model.PowerupType

/**
 * Draws powerups on the field.
 */
fun DrawScope.drawPowerups(powerups: List<Powerup>) {
    powerups.forEach { p ->
        val color = when (p.type) {
            PowerupType.ExtraLife -> Color.Green
            PowerupType.Shield -> Color.Blue
            PowerupType.SlowMotion -> Color.Magenta
        }

        drawCircle(
            color = color,
            radius = 10f,
            center = Offset(p.position.x, p.position.y)
        )
    }
}
