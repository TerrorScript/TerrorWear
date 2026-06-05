package com.terrsus.terrorwear.domain.games.pong.model

import com.terrsus.terrorwear.domain.games.pong.logic.PongRules
import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Creates the initial game state using screen size and playfield rules.
 *
 * @param screenWidth Width of the playfield.
 * @param screenHeight Height of the playfield.
 * @param rules Playfield boundaries.
 */
fun initialGameState(
    screenWidth: Float,
    screenHeight: Float,
    rules: PongRules
): GameState {

    val ball = Ball(
        position = Vector2(screenWidth / 2f, screenHeight / 2f),
        velocity = Vector2(120f, 80f),
        radius = 6f
    )

    val paddleWidth = 8f
    val paddleHeight = 40f

    val player = Paddle(
        position = Vector2(
            x = rules.left + 10f,
            y = (screenHeight - paddleHeight) / 2f
        ),
        width = paddleWidth,
        height = paddleHeight
    )

    val enemy = Paddle(
        position = Vector2(
            x = rules.right - paddleWidth - 10f,
            y = (screenHeight - paddleHeight) / 2f
        ),
        width = paddleWidth,
        height = paddleHeight
    )

    return GameState(
        ball = ball,
        playerPaddle = player,
        enemyPaddle = enemy,
        playerScore = 0,
        enemyScore = 0,
        totalTime = 0f
    )
}
