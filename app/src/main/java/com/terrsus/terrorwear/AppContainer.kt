package com.terrsus.terrorwear

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.runtime.staticCompositionLocalOf
import com.terrsus.terrorwear.features.ble.data.BleRepository
import com.terrsus.terrorwear.features.ble.data.BleRepositoryImpl
import com.terrsus.terrorwear.features.ble.domain.usecase.*
import com.terrsus.terrorwear.features.ble.insecure.*
import com.terrsus.terrorwear.features.ble.common.model.BleGattClientFake
import com.terrsus.terrorwear.features.ble.insecure.transport.BleGattClient
import com.terrsus.terrorwear.features.ble.insecure.transport.BleGattClientImpl
import com.terrsus.terrorwear.features.sensors.SensorManager
import com.terrsus.terrorwear.features.storage.domain.repository.*
import com.terrsus.terrorwear.features.storage.encryption.CryptoEngine
import com.terrsus.terrorwear.features.storage.encryption.KeyStoreManager
import com.terrsus.terrorwear.features.storage.filesystem.EncryptedFileStorage
import com.terrsus.terrorwear.features.storage.filesystem.FileStorage
import com.terrsus.terrorwear.features.storage.room.database.AppDatabase
import com.terrsus.terrorwear.features.storage.room.repository.HighscoreRepositoryRoomImpl
import com.terrsus.terrorwear.features.storage.room.repository.PairingKeyRepositoryRoomImpl
import com.terrsus.terrorwear.features.storage.room.repository.TrustedDeviceRepositoryRoomImpl
import com.terrsus.terrorwear.features.wifi.manager.WifiManager
import com.terrsus.terrorwear.features.wifi.data.WifiRepository
import com.terrsus.terrorwear.features.wifi.data.WifiRepositoryImpl
import com.terrsus.terrorwear.features.wifi.networkinfoprovider.WifiNetworkInfoProvider
import com.terrsus.terrorwear.features.wifi.networkinfoprovider.WifiNetworkInfoProviderFake
import com.terrsus.terrorwear.features.wifi.networkinfoprovider.WifiNetworkInfoProviderImpl
import com.terrsus.terrorwear.util.DeviceUtils
import java.io.File

/**
 * Central application-level service container.
 *
 * TerrorWear does not use a dependency-injection framework. Instead, this
 * object acts as a lightweight composition root that constructs and exposes
 * shared singletons such as BLE managers, Wi‑Fi managers, repositories,
 * encrypted storage, and use cases.
 *
 * Call [init] once from [App] before accessing any members.
 */
object AppContainer {
    init {
        Log.i("TW/AppContainer", "init")
    }

    private lateinit var appContext: Context

    /**
     * Initializes the container with the application context.
     *
     * Must be called exactly once during application startup.
     *
     * @param context Any context; the application context will be extracted.
     */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // -------------------------------------------------------------------------
    // Storage (Encrypted + Plain)
    // -------------------------------------------------------------------------

    /** Base directory for all app-managed storage. */
    private val storageDir: File by lazy {
        appContext.filesDir
    }

    /** Android Keystore-backed AES key manager. */
    private val keyStoreManager: KeyStoreManager by lazy {
        KeyStoreManager()
    }

    /** AES-GCM encryption engine for secure file storage. */
    private val cryptoEngine: CryptoEngine by lazy {
        CryptoEngine(keyStoreManager)
    }

    /** Encrypted file storage for sensitive data. */
    val encryptedStorage: EncryptedFileStorage by lazy {
        EncryptedFileStorage(storageDir, cryptoEngine)
    }

    /** Plain file storage for non-sensitive data. */
    val plainStorage: FileStorage by lazy {
        FileStorage(storageDir)
    }

    // -------------------------------------------------------------------------
    // Room Database + Repositories
    // -------------------------------------------------------------------------

    /** Shared Room database instance. */
    private val database: AppDatabase by lazy {
        AppDatabase.build(appContext)
    }

    /** Repository for trusted BLE devices. */
    val trustedDeviceRepository: TrustedDeviceRepository by lazy {
        TrustedDeviceRepositoryRoomImpl(database.trustedDeviceDao())
    }

    /** Repository for long-term BLE pairing keys. */
    val pairingKeyRepository: PairingKeyRepository by lazy {
        PairingKeyRepositoryRoomImpl(database.pairingKeyDao())
    }

    /** Repository for high-score persistence. */
    val highscoreRepository: HighscoreRepository by lazy {
        HighscoreRepositoryRoomImpl(database.highscoreDao())
    }

    // -------------------------------------------------------------------------
    // BLE
    // -------------------------------------------------------------------------

    /** BLE GATT client (fake on emulator). */
    val bleGattClient: BleGattClient by lazy {
        if (DeviceUtils.isEmulator) BleGattClientFake(appContext)
        else BleGattClientImpl(appContext)
    }

    /** BLE manager responsible for scanning and connecting. */
    val bleManager: BleManager by lazy {
        BleProvider.provide(appContext)
    }

    /** BLE repository exposing scanning and device flows. */
    val bleRepository: BleRepository by lazy {
        BleRepositoryImpl(bleManager)
    }

    // -------------------------------------------------------------------------
    // BLE Use Cases
    // -------------------------------------------------------------------------

    val observeBleDevicesUseCase by lazy { ObserveBleDevicesUseCase(bleRepository) }
    val observeBleScanningUseCase by lazy { ObserveBleScanningUseCase(bleRepository) }
    val startBleScanUseCase by lazy { StartBleScanUseCase(bleRepository) }
    val stopBleScanUseCase by lazy { StopBleScanUseCase(bleRepository) }

    // -------------------------------------------------------------------------
// Wi‑Fi
// -------------------------------------------------------------------------

    /** Wi‑Fi manager providing UDP, TCP client, and TCP server functionality. */
    val wifiManager: WifiManager by lazy { WifiManager() }

    /**
     * Provider for Wi‑Fi network metadata (SSID, RSSI, IP, etc.).
     *
     * Uses ConnectivityManager on real devices and a fake provider on emulators
     * where Wi‑Fi APIs are unavailable.
     */
    val wifiNetworkInfoProvider: WifiNetworkInfoProvider by lazy {
        if (DeviceUtils.isEmulator)
            WifiNetworkInfoProviderFake()
        else {
            val connectivityManager = appContext.getSystemService(ConnectivityManager::class.java)
            WifiNetworkInfoProviderImpl(connectivityManager)
        }
    }

    /** Wi‑Fi repository exposing packet flows and send/receive operations. */
    val wifiRepository: WifiRepository by lazy {
        WifiRepositoryImpl(wifiManager)
    }


    // -------------------------------------------------------------------------
    // Sensors
    // -------------------------------------------------------------------------

    /** Shared SensorManager instance used by tilt games and other features. */
    val sensorManager: SensorManager by lazy {
        SensorManager(appContext)
    }

    // -------------------------------------------------------------------------
    // Settings (DataStore)
    // -------------------------------------------------------------------------

    private val settingsStore by lazy {
        com.terrsus.terrorwear.features.settings.data.datastore.SettingsStore(
            com.terrsus.terrorwear.features.settings.data.datastore.KeyValueStore(
                com.terrsus.terrorwear.features.settings.data.datastore.DataStoreFactory.createSettingsStore(
                    appContext
                )
            )
        )
    }

    private val settingsRepository by lazy {
        com.terrsus.terrorwear.features.settings.data.repository.SettingsRepositoryImpl(
            settingsStore
        )
    }

    val settingsInteractor by lazy {
        com.terrsus.terrorwear.features.settings.domain.usecase.SettingsInteractor(
            settingsRepository
        )
    }
}

/**
 * CompositionLocal exposing the application's [AppContainer].
 *
 * This allows any composable to access shared dependencies without
 * relying on a DI framework.
 */
val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer not provided")
}