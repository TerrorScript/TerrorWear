package com.terrsus.terrorwear.domain.games.pong.logic

import com.terrsus.terrorwear.domain.games.pong.model.*
import com.terrsus.terrorwear.domain.math.Vector2
import kotlin.math.abs

private const val PLAYFIELD_MARGIN = 60f
private const val BALL_ACCELERATION = 1.05f

class PongRules(
    val screenWidth: Float,
    val screenHeight: Float
) {
    val left = PLAYFIELD_MARGIN
    val right = screenWidth - PLAYFIELD_MARGIN
    val top = PLAYFIELD_MARGIN
    val bottom = screenHeight - PLAYFIELD_MARGIN

    fun applyRules(state: GameState, deltaTime: Float): RuleResult {
        val ball = state.ball

        // 1. Scoring (highest priority)
        when (val score = detectScore(ball)) {
            is Collision.Score.Player -> return RuleResult(handleScore(state, score), Collision.Score.Player)
            is Collision.Score.Enemy -> return RuleResult(handleScore(state, score), Collision.Score.Enemy)
            else -> Unit
        }

        // 2. Wall collision
        if (detectWallCollision(ball)) {
            val bounced = bounceOffWall(ball)
            return RuleResult(state.copy(ball = bounced), Collision.Wall)
        }

        // 3. Paddle collision
        when {
            intersects(ball, state.playerPaddle) -> {
                val bounced = bounceOffPaddle(ball, isPlayer = true)
                return RuleResult(state.copy(ball = bounced), Collision.Paddle)
            }
            intersects(ball, state.enemyPaddle) -> {
                val bounced = bounceOffPaddle(ball, isPlayer = false)
                return RuleResult(state.copy(ball = bounced), Collision.Paddle)
            }
        }

        // 4. No collision
        return RuleResult(state, null)
    }

    // --- Collision Detection ---

    private fun detectScore(ball: Ball): Collision {
        return when {
            ball.position.x < left -> Collision.Score.Enemy
            ball.position.x > right -> Collision.Score.Player
            else -> Collision.None
        }
    }

    private fun detectWallCollision(ball: Ball): Boolean {
        return ball.position.y - ball.radius < top ||
                ball.position.y + ball.radius > bottom
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

    // --- Collision Response ---

    private fun bounceOffWall(ball: Ball): Ball {
        val newY = when {
            ball.position.y - ball.radius < top -> top + ball.radius
            ball.position.y + ball.radius > bottom -> bottom - ball.radius
            else -> ball.position.y
        }

        return ball.copy(
            position = ball.position.copy(y = newY),
            velocity = ball.velocity.copy(y = -ball.velocity.y)
        )
    }

    private fun bounceOffPaddle(ball: Ball, isPlayer: Boolean): Ball {
        val newVx = if (isPlayer) abs(ball.velocity.x) else -abs(ball.velocity.x)
        return ball.copy(
            velocity = Vector2(newVx, ball.velocity.y) * BALL_ACCELERATION
        )
    }

    private fun handleScore(state: GameState, score: Collision.Score): GameState {
        return when (score) {
            is Collision.Score.Player -> state.copy(
                playerScore = state.playerScore + 1,
                ball = resetBall(directionRight = false),
                lastCollision = score
            )
            is Collision.Score.Enemy -> state.copy(
                enemyScore = state.enemyScore + 1,
                ball = resetBall(directionRight = true),
                lastCollision = score
            )
        }
    }

    private fun resetBall(directionRight: Boolean): Ball {
        val speed = 140f
        val vx = if (directionRight) speed else -speed
        val vy = listOf(-80f, -40f, 40f, 80f).random()

        return Ball(
            position = Vector2(screenWidth / 2f, screenHeight / 2f),
            velocity = Vector2(vx, vy),
            radius = 6f
        )
    }
}