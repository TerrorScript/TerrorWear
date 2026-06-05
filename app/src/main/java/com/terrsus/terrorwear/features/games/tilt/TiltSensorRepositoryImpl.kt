package com.terrsus.terrorwear.features.games.tilt

import com.terrsus.terrorwear.domain.math.Vector2
import com.terrsus.terrorwear.features.sensors.SensorManager
import kotlin.math.max
import kotlin.math.min

/**
 * Converts raw orientation/acceleration data into a stable tilt vector
 * for the Tilt game.
 */
class TiltSensorRepositoryImpl(
    private val sensorManager: SensorManager
) : TiltSensorRepository {

    @Volatile
    private var tilt = Vector2(0f, 0f)

    @Volatile
    private var tapPending = false

    override fun start() {
        sensorManager.start()

        sensorManager.onOrientationChanged = { orientation ->
            // Map pitch/roll to X/Y tilt
            val x = orientation.roll / 45f   // normalize to [-1, 1]
            val y = orientation.pitch / 45f

            tilt = Vector2(
                x.coerceIn(-1f, 1f),
                y.coerceIn(-1f, 1f)
            )
        }

        sensorManager.onTap = {
            tapPending = true
        }
    }

    override fun stop() {
        sensorManager.stop()
    }

    override fun currentTilt(): Vector2 = tilt

    override fun consumeTap(): Boolean {
        val was = tapPending
        tapPending = false
        return was
    }
}
