package com.terrsus.terrorwear.ui.screens.games.pong.draw

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.terrsus.terrorwear.domain.games.pong.logic.PongRules
import com.terrsus.terrorwear.domain.games.pong.model.GameState

/**
 * Draws the field borders using the actual PongRules boundaries.
 */
fun DrawScope.drawField(state: GameState, rules: PongRules) {
    drawContext.canvas.nativeCanvas.apply {
        val paint = Paint().apply {
            color = Color.DKGRAY
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }

        // Top border
        drawLine(
            rules.left,
            rules.top,
            rules.right,
            rules.top,
            paint
        )

        // Bottom border
        drawLine(
            rules.left,
            rules.bottom,
            rules.right,
            rules.bottom,
            paint
        )
    }
}