package com.terrsus.terrorwear

import android.content.Context
import com.terrsus.terrorwear.data.repository.BleRepository
import com.terrsus.terrorwear.data.repository.BleRepositoryImpl
import com.terrsus.terrorwear.domain.usecase.*
import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.connection.BleProvider
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.WifiManager

object AppContainer {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // --- BLE Core ---
    val bleManager: BleManager by lazy {
        BleProvider.provide(appContext)
    }

    // --- Repository ---
    val bleRepository: BleRepository by lazy {
        BleRepositoryImpl(bleManager)
    }

    // --- Use Cases ---
    val observeBleDevicesUseCase by lazy {
        ObserveBleDevicesUseCase(bleRepository)
    }

    val observeBleStateUseCase by lazy {
        ObserveBleStateUseCase(bleRepository)
    }

    val startBleScanUseCase by lazy {
        StartBleScanUseCase(bleRepository)
    }

    val stopBleScanUseCase by lazy {
        StopBleScanUseCase(bleRepository)
    }

    // --- Other Features ---
    val wifiManager: WifiManager by lazy { WifiManager() }
    val sensorManager: SensorManager by lazy { SensorManager(appContext) }
}
