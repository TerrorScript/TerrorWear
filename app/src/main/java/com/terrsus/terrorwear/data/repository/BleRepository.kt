package com.terrsus.terrorwear.data.repository

import android.bluetooth.le.ScanResult
import kotlinx.coroutines.flow.StateFlow

interface BleRepository {
    val isScanning: StateFlow<Boolean>
    val scanResults: StateFlow<List<ScanResult>>

    fun startScan()
    fun stopScan()
}
