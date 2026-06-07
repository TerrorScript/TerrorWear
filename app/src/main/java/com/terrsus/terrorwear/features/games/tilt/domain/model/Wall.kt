package com.terrsus.terrorwear.features.games.tilt.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Static or temporary wall segment in the Tilt game.
 */
data class Wall(
    val id: Long,
    val start: Vector2,
    val end: Vector2
)
