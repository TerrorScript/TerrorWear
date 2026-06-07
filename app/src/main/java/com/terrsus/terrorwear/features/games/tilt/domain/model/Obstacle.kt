package com.terrsus.terrorwear.features.games.tilt.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

enum class ObstaclePhase {
    WARNING_BORDER,   // indicator along border only
    ACTIVE_IN_FIELD,  // moving toward center
    EXPIRED
}

data class Obstacle(
    val id: Long,
    val position: Vector2,
    val velocity: Vector2,
    val spawnedAtMillis: Long,
    val phase: ObstaclePhase
)
