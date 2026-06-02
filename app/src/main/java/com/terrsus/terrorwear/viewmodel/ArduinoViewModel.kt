package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.AppContainer
import kotlinx.coroutines.launch

class ArduinoViewModel : ViewModel() {

    private val startScan = AppContainer.startBleScan
    private val stopScan = AppContainer.stopBleScan

    val isScanning = AppContainer.bleRepository.isScanning
    val scanResults = AppContainer.bleRepository.scanResults

    fun beginScan() = viewModelScope.launch {
        startScan()
    }

    fun endScan() = viewModelScope.launch {
        stopScan()
    }
}
