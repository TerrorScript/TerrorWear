package com.terrsus.terrorwear

import android.content.Context
import com.terrsus.terrorwear.core.ble.BleClient
import com.terrsus.terrorwear.data.repository.BleRepositoryImpl
import com.terrsus.terrorwear.domain.usecase.StartBleScanUseCase
import com.terrsus.terrorwear.domain.usecase.StopBleScanUseCase

// Manual dependency "injection"; singleton which other files reference to.
object AppContainer {
    private lateinit var appContext: Context
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Core BLE components
    val bleClient by lazy { BleClient(appContext) }
    val bleRepository by lazy { BleRepositoryImpl(bleClient) }

    // Use cases
    val startBleScan by lazy { StartBleScanUseCase(bleRepository) }
    val stopBleScan by lazy { StopBleScanUseCase(bleRepository) }
}
