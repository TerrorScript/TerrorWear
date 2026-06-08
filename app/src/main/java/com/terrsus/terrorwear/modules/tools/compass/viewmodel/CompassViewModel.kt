package com.terrsus.terrorwear.modules.tools.compass.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Holds compass heading state.
 *
 * The heading is expressed in degrees [0f, 360f].
 * Sensor updates will eventually feed into [updateHeading].
 */
class CompassViewModel : ViewModel() {

    private val _heading = MutableStateFlow(0f)
    val heading: StateFlow<Float> = _heading.asStateFlow()

    /**
     * Update the compass heading.
     * This will be called by the sensor layer.
     */
    fun updateHeading(value: Float) {
        _heading.value = value
    }
}