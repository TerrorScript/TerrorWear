package com.terrsus.terrorwear.features.games.tilt.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Player-controlled ball in the Tilt game.
 */
data class Ball(
    val position: Vector2,
    val velocity: Vector2,
    val radius: Float = 10f
)
