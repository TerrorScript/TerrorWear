package com.terrsus.terrorwear.features.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import android.hardware.SensorManager as AndroidSensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Centralized access to device sensors.
 *
 * Provides orientation (yaw, pitch, roll), linear acceleration,
 * and a simple tap callback. This class talks directly to Android's
 * hardware APIs and exposes raw sensor data as flows.
 */
class SensorManager(context: Context) : SensorEventListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val android = context.getSystemService(Context.SENSOR_SERVICE) as AndroidSensorManager

    /** Latest orientation values in degrees. */
    private val _orientation = MutableStateFlow(OrientationData(0f, 0f, 0f))
    val orientation: StateFlow<OrientationData> = _orientation

    /** Latest linear acceleration values in m/s². */
    private val _accel = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration: StateFlow<AccelerationData> = _accel

    /** Convenience heading value (yaw only). */
    private val _heading = MutableStateFlow(0f)
    val heading: StateFlow<Float> = _heading

    /** Optional callback for game logic when orientation changes. */
    var onOrientationChanged: ((OrientationData) -> Unit)? = null

    /** Optional callback for tap gestures. */
    var onTap: (() -> Unit)? = null

    init {
        // Keep heading updated whenever orientation changes.
        onOrientationChanged = { data ->
            _heading.value = data.yaw
        }
    }

    // Sensors we listen to.
    private val rotationVector = android.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private val linearAccel = android.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    private val tapSensor = android.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)

    /**
     * Start receiving sensor updates.
     *
     * Registers listeners for rotation vector, linear acceleration,
     * and tap detection.
     */
    fun start() {
        rotationVector?.let {
            android.registerListener(this, it, AndroidSensorManager.SENSOR_DELAY_GAME)
        }
        linearAccel?.let {
            android.registerListener(this, it, AndroidSensorManager.SENSOR_DELAY_GAME)
        }
        tapSensor?.let {
            android.registerListener(this, it, AndroidSensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    /**
     * Stop receiving sensor updates.
     *
     * Unregisters all listeners and cancels background work.
     */
    fun stop() {
        android.unregisterListener(this)
        scope.coroutineContext.cancelChildren()
    }

    /**
     * Called by Android when a sensor value changes.
     *
     * @param event The sensor event containing updated values.
     */
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {

            Sensor.TYPE_ROTATION_VECTOR -> {
                val data = OrientationData.fromRotationVector(event.values)
                Log.d("SensorManager", "onSensorChanged TYPE_ROTATION_VECTOR $data}")
                _orientation.value = data
                onOrientationChanged?.invoke(data)
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                Log.d("SensorManager", "onSensorChanged TYPE_LINEAR_ACCELERATION ${event.values}")
                _accel.value = AccelerationData(
                    x = event.values[0],
                    y = event.values[1],
                    z = event.values[2]
                )
            }

            Sensor.TYPE_SIGNIFICANT_MOTION -> {
                Log.d("SensorManager", "onSensorChanged TYPE_SIGNIFICANT_MOTION")
                onTap?.invoke()
            }
        }
    }

    /**
     * Accuracy changes are ignored.
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used.
    }
}