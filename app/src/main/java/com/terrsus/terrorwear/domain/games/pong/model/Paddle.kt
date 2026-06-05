package com.terrsus.terrorwear.domain.games.pong.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Represents a paddle in the Pong game.
 */
data class Paddle(
    val position: Vector2,
    val width: Float,
    val height: Float
)
