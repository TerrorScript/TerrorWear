package com.terrsus.terrorwear.domain.games.tilt.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Complete immutable world state for the Tilt game.
 *
 * This contains ONLY persistent game-world data.
 * No input, no timers, no transient values.
 *
 * Updated exclusively by TiltRules.update().
 */
data class GameState(

    /** Player-controlled ball. */
    val ball: Ball,

    /** Active obstacles (warning or active). */
    val obstacles: List<Obstacle>,

    /** Temporary walls created by tap/jump (optional). */
    val walls: List<Wall>,

    /** Active powerups on the field. */
    val powerups: List<Powerup>,

    /** Remaining lives (0–3). */
    val lives: Int,

    /** Timestamp of last life loss (for regen). */
    val lastLifeLostAt: Long?,

    /** If non-null, player is invulnerable until this timestamp. */
    val jumpUntilMillis: Long?,

    /** Timestamp of last obstacle spawn (for spawn pacing). */
    val lastObstacleSpawnedAtMillis: Long
)
