package com.terrsus.terrorwear.features.games.pong.domain.logic

import com.terrsus.terrorwear.features.games.pong.domain.model.GameState
import com.terrsus.terrorwear.features.games.pong.domain.model.Paddle

/**
 * Simple AI controller for the enemy paddle.
 *
 * @param speed Maximum paddle movement speed (px/sec).
 * @param reaction How strongly the paddle reacts to the ball's position.
 */
class PongAi(
    private val speed: Float = 120f,
    private val reaction: Float = 0.15f
) {

    /**
     * Updates the enemy paddle position based on ball location.
     *
     * @param state Current game state.
     * @param deltaTime Time step in seconds.
     * @param rules Playfield boundaries.
     */
    fun updateEnemy(state: GameState, deltaTime: Float, rules: PongRules): Paddle {
        val paddle = state.enemyPaddle
        val ball = state.ball

        // Target vertical center of the ball
        val targetY = ball.position.y - paddle.height / 2f
        val dy = targetY - paddle.position.y

        // Limit movement by AI speed
        val maxMove = speed * deltaTime
        val actualMove = (dy * reaction).coerceIn(-maxMove, maxMove)

        val newY = paddle.position.y + actualMove

        // Clamp to playfield
        val clampedY = newY.coerceIn(
            rules.top,
            rules.bottom - paddle.height
        )

        return paddle.copy(
            position = paddle.position.copy(y = clampedY)
        )
    }
}
