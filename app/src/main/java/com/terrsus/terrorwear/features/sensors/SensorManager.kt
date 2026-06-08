package com.terrsus.terrorwear.features.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager as AndroidSensorManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Centralized sensor hub for TerrorWear.
 *
 * Provides:
 * - OrientationData (pitch, roll, yaw)
 * - AccelerationData (x, y, z)
 * - Tap gesture callback
 *
 * This class belongs in the *data layer* because it interacts with
 * Android hardware APIs and exposes raw sensor information.
 *
 * Higher layers (ViewModels, game logic) consume only the processed
 * values via TiltSensorRepository or other repositories.
 */
class SensorManager(context: Context) : SensorEventListener {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val android = context.getSystemService(Context.SENSOR_SERVICE) as AndroidSensorManager

    // --- Public flows --------------------------------------------------------

    private val _orientation = MutableStateFlow(OrientationData(0f, 0f, 0f))
    val orientation: StateFlow<OrientationData> = _orientation


    private val _accel = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration: StateFlow<AccelerationData> = _accel

    // --- Optional callbacks for game repositories ----------------------------

    /** Called whenever orientation changes. */
    var onOrientationChanged: ((OrientationData) -> Unit)? = null
// Inside SensorManager

    private val _heading = MutableStateFlow(0f)
    val heading: StateFlow<Float> = _heading

    init {
        onOrientationChanged = { data ->
            _heading.value = data.yaw
        }
    }

    /** Called when a tap gesture is detected. */
    var onTap: (() -> Unit)? = null

    // --- Internal sensors ----------------------------------------------------

    private val rotationVector =
        android.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    private val linearAccel =
        android.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    // TODO: Replace with proper gesture detector if needed
    private val tapSensor =
        android.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION)

    // --- Lifecycle -----------------------------------------------------------

    /**
     * Starts listening to all required sensors.
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
     * Stops all sensor listeners and cancels internal coroutines.
     */
    fun stop() {
        android.unregisterListener(this)
        scope.coroutineContext.cancelChildren()
    }

    // --- SensorEventListener -------------------------------------------------

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {

            Sensor.TYPE_ROTATION_VECTOR -> {
                val orientationData = OrientationData.fromRotationVector(event.values)
                _orientation.value = orientationData
                onOrientationChanged?.invoke(orientationData)
            }

            Sensor.TYPE_LINEAR_ACCELERATION -> {
                val accelData = AccelerationData(
                    x = event.values[0],
                    y = event.values[1],
                    z = event.values[2]
                )
                _accel.value = accelData
            }

            Sensor.TYPE_SIGNIFICANT_MOTION -> {
                onTap?.invoke()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}