package com.terrsus.terrorwear.domain.games.tilt.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Creates the initial Tilt game state.
 */
fun initialGameState(nowMillis: Long): GameState {
    return GameState(
        ball = Ball(
            position = Vector2(0f, 0f),
            velocity = Vector2(0f, 0f)
        ),
        obstacles = emptyList(),
        walls = emptyList(),
        powerups = emptyList(),
        lives = 3,
        lastLifeLostAt = null,
        jumpUntilMillis = null,
        lastObstacleSpawnedAtMillis = nowMillis
    )
}
