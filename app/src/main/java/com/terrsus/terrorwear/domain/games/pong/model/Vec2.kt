package com.terrsus.terrorwear.domain.games.pong.model

data class Vec2(val x: Float, val y: Float) {
    operator fun times(factor: Float) = Vec2(x * factor, y * factor)
}