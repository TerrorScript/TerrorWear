package com.terrsus.terrorwear.features.games.tilt.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Types of powerups that can appear in the Tilt game.
 */
enum class PowerupType {
    ExtraLife,
    Shield,
    SlowMotion
}

/**
 * Powerup entity on the playfield.
 */
data class Powerup(
    val id: Long,
    val position: Vector2,
    val type: PowerupType,
    val isActive: Boolean
)
