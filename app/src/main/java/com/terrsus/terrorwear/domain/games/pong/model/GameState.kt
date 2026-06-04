package com.terrsus.terrorwear.domain.games.pong.model


data class GameState(
    val ball: Ball,
    val playerPaddle: Paddle,
    val enemyPaddle: Paddle,

    val playerScore: Int = 0,
    val enemyScore: Int = 0,

    val totalTime: Float = 0f,
) {
    companion object {
        fun placeholder(): GameState {
            return GameState(
                ball = Ball(Vec2(0f, 0f), Vec2(0f, 0f), 0f),
                playerPaddle = Paddle(Vec2(0f, 0f), 0f, 0f),
                enemyPaddle = Paddle(Vec2(0f, 0f), 0f, 0f),
                playerScore = 0,
                enemyScore = 0,
                totalTime = 0f
            )
        }
    }
}
