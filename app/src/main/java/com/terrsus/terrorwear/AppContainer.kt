package com.terrsus.terrorwear

import android.content.Context
import com.terrsus.terrorwear.data.ble.BleRepository
import com.terrsus.terrorwear.data.ble.BleRepositoryImpl
import com.terrsus.terrorwear.data.wifi.WifiRepository
import com.terrsus.terrorwear.data.wifi.WifiRepositoryImpl
import com.terrsus.terrorwear.domain.usecase.*
import com.terrsus.terrorwear.features.ble.connection.BleManager
import com.terrsus.terrorwear.features.ble.connection.BleProvider
import com.terrsus.terrorwear.features.ble.gatt.BleGattClientImpl
import com.terrsus.terrorwear.features.ble.gatt.BleGattClient
import com.terrsus.terrorwear.features.ble.gatt.FakeBleGattClient
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.wifi.WifiManager
import com.terrsus.terrorwear.util.DeviceUtils

/**
 * Global dependency container for the application.
 *
 * This object provides shared singletons for BLE, Wi‑Fi, sensors,
 * and related repositories and use cases. All components that require
 * an Android context are initialized with the application context.
 *
 * Call [init] once from the Application class before accessing any members.
 */
object AppContainer {

    private lateinit var appContext: Context

    /**
     * Initializes the container with the application context.
     *
     * @param context any context, the application context will be extracted
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /**
     * BLE GATT client used for reading, writing and subscribing to characteristics.
     * Uses a fake client on emulators.
     */
    val bleGattClient: BleGattClient by lazy {
        if (DeviceUtils.isEmulator()) FakeBleGattClient(appContext)
        else BleGattClientImpl(appContext)
    }

    /**
     * BLE manager responsible for scanning and connecting to BLE devices.
     */
    val bleManager: BleManager by lazy {
        BleProvider.provide(appContext)
    }

    /**
     * BLE repository that exposes scanning and device flows.
     */
    val bleRepository: BleRepository by lazy {
        BleRepositoryImpl(bleManager)
    }

    /**
     * Wi‑Fi manager that provides UDP, TCP client and TCP server functionality.
     */
    val wifiManager: WifiManager by lazy {
        WifiManager()
    }

    /**
     * Wi‑Fi repository that exposes packet flows and send/receive operations.
     */
    val wifiRepository: WifiRepository by lazy {
        WifiRepositoryImpl(wifiManager)
    }

    /**
     * Use case for observing BLE scan results.
     */
    val observeBleDevicesUseCase by lazy {
        ObserveBleDevicesUseCase(bleRepository)
    }

    /**
     * Use case for observing BLE scanning state.
     */
    val observeBleScanningUseCase by lazy {
        ObserveBleScanningUseCase(bleRepository)
    }

    /**
     * Use case for starting BLE scanning.
     */
    val startBleScanUseCase by lazy {
        StartBleScanUseCase(bleRepository)
    }

    /**
     * Use case for stopping BLE scanning.
     */
    val stopBleScanUseCase by lazy {
        StopBleScanUseCase(bleRepository)
    }

    /**
     * Shared SensorManager instance used by tilt games and other features.
     * Initialized once and reused across screens.
     */
    val sensorManager: SensorManager by lazy {
        SensorManager(appContext)
    }
}