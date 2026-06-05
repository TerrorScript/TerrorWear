package com.terrsus.terrorwear.domain.math

/**
 * Simple 2D vector used for positions and velocities.
 */
data class Vector2(val x: Float, val y: Float) {
    operator fun times(factor: Float) = Vector2(x * factor, y * factor)
}