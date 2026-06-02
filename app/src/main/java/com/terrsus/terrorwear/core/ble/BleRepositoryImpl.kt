package com.terrsus.terrorwear.data.repository

import com.terrsus.terrorwear.core.ble.BleClient

class BleRepositoryImpl(
    private val client: BleClient
) : BleRepository {

    override val isScanning = client.isScanning
    override val scanResults = client.scanResults

    override fun startScan() = client.startScan()
    override fun stopScan() = client.stopScan()
}
