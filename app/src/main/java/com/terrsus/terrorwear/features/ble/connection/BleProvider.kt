package com.terrsus.terrorwear.features.ble.connection

import android.content.Context
import com.terrsus.terrorwear.features.ble.scanner.BleScannerClient

object BleProvider {
    fun provide(context: Context): BleManager {
        val client = BleScannerClient(context)
        return BleManager(client) // use the same scanner
    }
}