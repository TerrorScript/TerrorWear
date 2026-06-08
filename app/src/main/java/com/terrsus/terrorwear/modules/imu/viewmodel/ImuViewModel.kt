package com.terrsus.terrorwear.modules.imu.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds IMU sensor state.
 *
 * Values are raw sensor outputs (m/s², rad/s, µT) and a fused orientation
 * expressed as Euler angles in degrees.
 */
class ImuViewModel : ViewModel() {

    data class ImuState(
        val accel: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
        val gyro: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
        val mag: Triple<Float, Float, Float> = Triple(0f, 0f, 0f),
        val orientation: Triple<Float, Float, Float> = Triple(0f, 0f, 0f) // yaw, pitch, roll
    )

    private val _state = MutableStateFlow(ImuState())
    val state: StateFlow<ImuState> = _state.asStateFlow()

    fun updateAccel(x: Float, y: Float, z: Float) {
        _state.value = _state.value.copy(accel = Triple(x, y, z))
    }

    fun updateGyro(x: Float, y: Float, z: Float) {
        _state.value = _state.value.copy(gyro = Triple(x, y, z))
    }

    fun updateMag(x: Float, y: Float, z: Float) {
        _state.value = _state.value.copy(mag = Triple(x, y, z))
    }

    fun updateOrientation(yaw: Float, pitch: Float, roll: Float) {
        _state.value = _state.value.copy(orientation = Triple(yaw, pitch, roll))
    }
}