package com.terrsus.terrorwear.domain.math

/**
 * Simple 2D vector used for positions and velocities.
 */
data class Vector2(val x: Float, val y: Float) {
    operator fun times(factor: Float) = Vector2(x * factor, y * factor)

    operator fun div(denominator: Float) = Vector2(x / denominator, y / denominator)
    operator fun div(other: Vector2) = Vector2(x / other.x, y / other.y)

    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
}