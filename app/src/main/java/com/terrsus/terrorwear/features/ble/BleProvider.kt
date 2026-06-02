package com.terrsus.terrorwear.features.ble

import android.content.Context

object BleProvider {
    fun provide(context: Context): BleManager {
        val client = BleClient(context)
        val scanner = BleScanner(context)
        return BleManager(client, scanner)
    }
}