package com.terrsus.terrorwear.domain.games.pong.model

sealed class Collision {
    data object None : Collision()
    data object Wall : Collision()
    data object Paddle : Collision()

    sealed class Score : Collision() {
        data object Player : Score()      // player scored
        data object Enemy : Score()       // enemy scored
    }
}