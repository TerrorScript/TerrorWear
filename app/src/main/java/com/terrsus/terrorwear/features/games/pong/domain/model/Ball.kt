package com.terrsus.terrorwear.features.games.pong.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Represents the Pong ball.
 *
 * @param position Current position.
 * @param velocity Current velocity.
 * @param radius Ball radius in pixels.
 */
data class Ball(
    val position: Vector2,
    val velocity: Vector2,
    val radius: Float
)
