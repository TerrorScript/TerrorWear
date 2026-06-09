package com.terrsus.terrorwear.features.ble.insecure.scanner

import android.util.Log
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val LogTag = "TW/BLE/Scanner"

val fakeList = listOf(
    BleDevice("AA:BB:CC:DD:EE:01", "Arduino Uno", -42),
    BleDevice("AA:BB:CC:DD:EE:02", "Arduino Nano 33 BLE", -55),
    BleDevice("AA:BB:CC:DD:EE:03", "ESP32 DevKit", -60),
    BleDevice("AA:BB:CC:DD:EE:04", "HM-10 BLE Module", -70),
    BleDevice("AA:BB:CC:DD:EE:05", "Nordic UART Service", -50)
)

class BleScannerFake : BleScanner {
    init {
        Log.d(LogTag, "init FakeBleScanner, created instance = ${this.hashCode()}")
    }

    override val isScanning = MutableStateFlow(false)
    override val scanResults = MutableStateFlow(fakeList)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun startScan() {
        Log.d(LogTag, "scan starting")

        isScanning.value = true

        // Emit extra devices over time
        scope.launch {
            for (i in 1..5) {
                val ms = (800..1600).random().toLong()
                delay(ms)

                addDevice("00:00:00:DD:00:0$i", "Some device $i", (-90..-10).random())
            }
        }

        Log.d(LogTag, "scan started")
    }

    override fun stopScan() {
        Log.d(LogTag, "scan stopping")

        isScanning.value = false

        Log.d(LogTag, "scan stopped")
    }

    private fun addDevice(address: String, name: String, rssi: Int) {
        Log.d(LogTag, "device adding address=$address name=$name rssi=$rssi")

        val updated = scanResults.value.toMutableList()
        val device = BleDevice(address, name, rssi)

        updated.add(device)
        scanResults.value = updated

        Log.d(LogTag, "device added address=$address name=$name rssi=$rssi")
    }
}