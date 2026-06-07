package com.terrsus.terrorwear.features.games.pong.domain.model

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Holds all game state for a single frame.
 */
data class GameState(
    val ball: Ball,
    val playerPaddle: Paddle,
    val enemyPaddle: Paddle,
    val playerScore: Int = 0,
    val enemyScore: Int = 0,
    val totalTime: Float = 0f,
    val lastCollision: Collision = Collision.None
) {
    companion object {
        /**
         * Returns an empty placeholder state used before initialization.
         */
        fun placeholder(): GameState {
            return GameState(
                ball = Ball(Vector2(0f, 0f), Vector2(0f, 0f), 0f),
                playerPaddle = Paddle(Vector2(0f, 0f), 0f, 0f),
                enemyPaddle = Paddle(Vector2(0f, 0f), 0f, 0f),
                playerScore = 0,
                enemyScore = 0,
                totalTime = 0f
            )
        }
    }
}
