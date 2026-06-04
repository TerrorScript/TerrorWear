package com.terrsus.terrorwear.domain.games.pong.logic

import com.terrsus.terrorwear.domain.games.pong.model.Ball
import com.terrsus.terrorwear.domain.games.pong.model.GameState
import com.terrsus.terrorwear.domain.games.pong.model.Paddle
import com.terrsus.terrorwear.domain.games.pong.model.Vec2
import kotlin.math.abs

private const val PLAYFIELD_MARGIN = 60f
private const val BALL_ACCELERATION = 1.05f   // +5% per paddle hit

class PongRules(
    val screenWidth: Float,
    val screenHeight: Float
) {
    val left = PLAYFIELD_MARGIN
    val right = screenWidth - PLAYFIELD_MARGIN
    val top = PLAYFIELD_MARGIN
    val bottom = screenHeight - PLAYFIELD_MARGIN

    fun applyRules(state: GameState, dt: Float): GameState {
        var newState = state

        newState = bounceOffWalls(newState)
        newState = bounceOffPaddles(newState)
        newState = checkScore(newState)

        return newState
    }

    private fun bounceOffWalls(state: GameState): GameState {
        var ball = state.ball

        if (ball.position.y - ball.radius < top) {
            ball = ball.copy(
                position = ball.position.copy(y = top + ball.radius),
                velocity = ball.velocity.copy(y = abs(ball.velocity.y))
            )
        }

        if (ball.position.y + ball.radius > bottom) {
            ball = ball.copy(
                position = ball.position.copy(y = bottom - ball.radius),
                velocity = ball.velocity.copy(y = -abs(ball.velocity.y))
            )
        }

        return state.copy(ball = ball)
    }

    private fun bounceOffPaddles(state: GameState): GameState {
        val ball = state.ball

        if (intersects(ball, state.playerPaddle)) {
            val bounced = ball.copy(
                velocity = ball.velocity.copy(x = abs(ball.velocity.x)) * BALL_ACCELERATION
            )
            return state.copy(ball = bounced)
        }

        if (intersects(ball, state.enemyPaddle)) {
            val bounced = ball.copy(
                velocity = ball.velocity.copy(x = -abs(ball.velocity.x)) * BALL_ACCELERATION
            )
            return state.copy(ball = bounced)
        }

        return state
    }

    private fun intersects(ball: Ball, paddle: Paddle): Boolean {
        val withinX =
            ball.position.x + ball.radius > paddle.position.x &&
                    ball.position.x - ball.radius < paddle.position.x + paddle.width

        val withinY =
            ball.position.y + ball.radius > paddle.position.y &&
                    ball.position.y - ball.radius < paddle.position.y + paddle.height

        return withinX && withinY
    }

    private fun checkScore(state: GameState): GameState {
        val ball = state.ball

        if (ball.position.x < left) {
            return state.copy(
                enemyScore = state.enemyScore + 1,
                ball = resetBall(directionRight = true)
            )
        }

        if (ball.position.x > right) {
            return state.copy(
                playerScore = state.playerScore + 1,
                ball = resetBall(directionRight = false)
            )
        }

        return state
    }

    private fun resetBall(directionRight: Boolean): Ball {
        val speed = 140f
        val vx = if (directionRight) speed else -speed
        val vy = listOf(-80f, -40f, 40f, 80f).random()

        return Ball(
            position = Vec2(screenWidth / 2f, screenHeight / 2f),
            velocity = Vec2(vx, vy),
            radius = 6f
        )
    }
}
