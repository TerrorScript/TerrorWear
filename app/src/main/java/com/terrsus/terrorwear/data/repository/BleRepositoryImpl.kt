package com.terrsus.terrorwear.data.repository

import android.annotation.SuppressLint
import com.terrsus.terrorwear.features.ble.BleClient
import com.terrsus.terrorwear.features.ble.BleDevice
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted

class BleRepositoryImpl(
    private val client: BleClient
) : BleRepository {

    private val scope = CoroutineScope(Dispatchers.Default)

    override val isScanning: StateFlow<Boolean> = client.isScanning

    @SuppressLint("MissingPermission")
    override val scanResults: StateFlow<List<BleDevice>> =
        client.scanResults
            .map { list ->
                list.map { scan ->
                    BleDevice(
                        address = scan.device.address,
                        name = scan.device.name,
                        rssi = scan.rssi
                    )
                }
            }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override fun startScan() = client.startScan()
    override fun stopScan() = client.stopScan()
}
