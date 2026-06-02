package com.terrsus.terrorwear.features.ble.scanner

import android.util.Log
import com.terrsus.terrorwear.features.ble.model.BleDevice
import kotlinx.coroutines.flow.MutableStateFlow

val fakeList = listOf(
    BleDevice(
        address = "AA:BB:CC:DD:EE:01",
        name = "Arduino Uno",
        rssi = -42
    ),
    BleDevice(
        address = "AA:BB:CC:DD:EE:02",
        name = "Arduino Nano 33 BLE",
        rssi = -55
    ),
    BleDevice(
        address = "AA:BB:CC:DD:EE:03",
        name = "ESP32 DevKit",
        rssi = -60
    ),
    BleDevice(
        address = "AA:BB:CC:DD:EE:04",
        name = "HM-10 BLE Module",
        rssi = -70
    ),
    BleDevice(
        address = "AA:BB:CC:DD:EE:05",
        name = "Nordic UART Service",
        rssi = -50
    )
)


class FakeBleScanner() : BleScanner {
    override val isScanning = MutableStateFlow(false)
    override val scanResults = MutableStateFlow(fakeList)

    override fun startScan() {
        isScanning.value = true
    }

    override fun stopScan() {
        isScanning.value = false
    }

    init {
        Log.d("BleScanner", "FakeBleScanner created")
    }
}
