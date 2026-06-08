package com.terrsus.terrorwear.modules.tools.cameraremote.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraRemoteViewModel : ViewModel() {

    private val _flashEnabled = MutableStateFlow(false)
    val flashEnabled: StateFlow<Boolean> = _flashEnabled

    private val _zoom = MutableStateFlow(1.0f)
    val zoom: StateFlow<Float> = _zoom

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    fun toggleFlash() {
        _flashEnabled.value = !_flashEnabled.value
        _status.value = if (_flashEnabled.value) "Flash On" else "Flash Off"
    }

    fun switchCamera() {
        _status.value = "Switched Camera"
    }

    fun takePhoto() {
        _status.value = "Shutter"
    }

    fun setZoom(value: Float) {
        _zoom.value = value
        _status.value = "Zoom: ${(value * 100).toInt()}%"
    }

    fun clearStatus() {
        _status.value = ""
    }
}
