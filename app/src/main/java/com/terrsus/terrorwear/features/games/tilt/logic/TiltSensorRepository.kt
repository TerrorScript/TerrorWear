package com.terrsus.terrorwear.features.games.tilt.logic

import com.terrsus.terrorwear.domain.math.Vector2

/**
 * Provides wrist tilt and tap input for the Tilt game.
 *
 * Implemented using the shared SensorManager in features/sensors.
 */
interface TiltSensorRepository {

    /** Returns the current wrist tilt vector, normalized to [-1, 1]. */
    fun currentTilt(): Vector2

    /** Returns true once when a tap gesture is detected. */
    fun consumeTap(): Boolean

    /** Starts listening to sensors. */
    fun start()

    /** Stops listening to sensors. */
    fun stop()
}
