package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.domain.usecase.StartBleScanUseCase
import com.terrsus.terrorwear.domain.usecase.StopBleScanUseCase
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArduinoViewModel(
    private val startScan: StartBleScanUseCase,
    private val stopScan: StopBleScanUseCase,
    val isScanning: StateFlow<Boolean>,
    val scanResults: StateFlow<List<android.bluetooth.le.ScanResult>>
) : ViewModel() {

    fun beginScan() = viewModelScope.launch {
        startScan()
    }

    fun endScan() = viewModelScope.launch {
        stopScan()
    }
}
