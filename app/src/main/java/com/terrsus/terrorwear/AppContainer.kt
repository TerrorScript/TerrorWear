package com.terrsus.terrorwear

import android.content.Context
import com.terrsus.terrorwear.data.repository.BleRepository
import com.terrsus.terrorwear.data.repository.BleRepositoryImpl
import com.terrsus.terrorwear.domain.usecase.*
import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.connection.BleProvider
import com.terrsus.terrorwear.features.ble.gatt.BleGattClientImpl
import com.terrsus.terrorwear.features.ble.gatt.BleGattClient
import com.terrsus.terrorwear.features.ble.gatt.FakeBleGattClient
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.WifiManager
import com.terrsus.terrorwear.util.DeviceUtils

object AppContainer {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    val bleGattClient: BleGattClient by lazy {
        if (DeviceUtils.isEmulator()) FakeBleGattClient(appContext)
        else BleGattClientImpl(appContext)
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

    val observeBleScanningUseCase by lazy {
        ObserveBleScanningUseCase(bleRepository)
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
