package com.terrsus.terrorwear

import android.content.Context
import com.terrsus.terrorwear.features.ble.BleManager
import com.terrsus.terrorwear.features.ble.BleProvider
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.WifiManager

object AppContainer {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // --- Feature Managers ---
    val bleManager: BleManager by lazy {
        BleProvider.provide(appContext)
    }

    val wifiManager: WifiManager by lazy {
        WifiManager()
    }

    val sensorManager: SensorManager by lazy {
        SensorManager(appContext)
    }
}