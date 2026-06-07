package com.terrsus.terrorwear.features.games.pong.domain.logic

import com.terrsus.terrorwear.features.games.pong.domain.model.Ball
import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Handles basic ball physics such as movement and simple collisions.
 */
class PongPhysics {

    /**
     * Moves the ball based on its velocity.
     *
     * @param ball Current ball state.
     * @param deltaTime Time step in seconds.
     */
    fun updateBall(ball: Ball, deltaTime: Float): Ball {
        return ball.copy(
            position = Vector2(
                ball.position.x + ball.velocity.x * deltaTime,
                ball.position.y + ball.velocity.y * deltaTime
            )
        )
    }
}