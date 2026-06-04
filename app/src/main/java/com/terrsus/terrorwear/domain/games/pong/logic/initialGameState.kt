package com.terrsus.terrorwear.domain.games.pong.logic

import com.terrsus.terrorwear.domain.games.pong.model.Ball
import com.terrsus.terrorwear.domain.games.pong.model.GameState
import com.terrsus.terrorwear.domain.games.pong.model.Paddle
import com.terrsus.terrorwear.domain.games.pong.model.Vec2

fun initialGameState(
    screenWidth: Float,
    screenHeight: Float,
    rules: PongRules
): GameState {

    val ball = Ball(
        position = Vec2(screenWidth / 2f, screenHeight / 2f),
        velocity = Vec2(120f, 80f),
        radius = 6f
    )

    val paddleWidth = 8f
    val paddleHeight = 40f

    val player = Paddle(
        position = Vec2(
            x = rules.left + 10f, // inset from safe boundary
            y = (screenHeight - paddleHeight) / 2f
        ),
        width = paddleWidth,
        height = paddleHeight
    )

    val enemy = Paddle(
        position = Vec2(
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
