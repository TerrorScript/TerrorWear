package com.terrsus.terrorwear.domain.games.pong.logic

import com.terrsus.terrorwear.domain.games.pong.model.Ball
import com.terrsus.terrorwear.domain.games.pong.model.Paddle
import com.terrsus.terrorwear.domain.games.pong.model.Vec2

class PongPhysics {

    fun updateBall(ball: Ball, deltaTime: Float): Ball {
        return ball.copy(
            position = Vec2(
                ball.position.x + ball.velocity.x * deltaTime,
                ball.position.y + ball.velocity.y * deltaTime
            )
        )
    }

    fun bounceOffWalls(ball: Ball, screenHeight: Float): Ball {
        if (ball.position.y - ball.radius < 0 || ball.position.y + ball.radius > screenHeight) {
            return ball.copy(velocity = ball.velocity.copy(y = -ball.velocity.y))
        }
        return ball
    }

    fun bounceOffPaddle(ball: Ball, paddle: Paddle): Ball {
        val withinX = ball.position.x - ball.radius < paddle.position.x + paddle.width &&
                ball.position.x + ball.radius > paddle.position.x
        val withinY = ball.position.y > paddle.position.y &&
                ball.position.y < paddle.position.y + paddle.height

        return if (withinX && withinY) {
            ball.copy(velocity = ball.velocity.copy(x = -ball.velocity.x))
        } else ball
    }
}
