package com.terrsus.terrorwear.features.ble.insecure

import android.content.Context
import com.terrsus.terrorwear.features.ble.insecure.scanner.BleScannerClient

object BleProvider {
    fun provide(context: Context): BleManager {
        val client = BleScannerClient(context)
        return BleManager(client) // use the same scanner
    }
}