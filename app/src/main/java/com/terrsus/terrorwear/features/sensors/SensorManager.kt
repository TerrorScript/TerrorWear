package com.terrsus.terrorwear.features.sensors

import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SensorManager(context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _orientation = MutableStateFlow(OrientationData(0f, 0f, 0f))
    val orientation: StateFlow<OrientationData> = _orientation

    private val _accel = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration: StateFlow<AccelerationData> = _accel

    fun start() {
        // TODO: register listeners
    }

    fun stop() {
        scope.coroutineContext.cancelChildren()
    }
}
