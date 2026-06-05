package com.terrsus.terrorwear.features.sensors

import android.hardware.SensorManager

/**
 * Orientation in degrees.
 */
data class OrientationData(
    val pitch: Float,
    val roll: Float,
    val yaw: Float
) {
    companion object {
        fun fromRotationVector(values: FloatArray): OrientationData {
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, values)

            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            return OrientationData(
                pitch = Math.toDegrees(orientation[1].toDouble()).toFloat(),
                roll = Math.toDegrees(orientation[2].toDouble()).toFloat(),
                yaw = Math.toDegrees(orientation[0].toDouble()).toFloat()
            )
        }
    }
}