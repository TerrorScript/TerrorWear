package com.terrsus.terrorwear.features.ble.secure.manager

import android.util.Log
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.secure.session.SecureBleConnectionState
import com.terrsus.terrorwear.features.ble.secure.transport.SecureBleGattClient
import com.terrsus.terrorwear.features.storage.domain.usecase.PairingKeyInteractor
import com.terrsus.terrorwear.features.storage.domain.usecase.TrustedDeviceInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val LogTag = "TW/BLE/SecureBleManager"

/**
 * Implementation of SecureBleManager with storage integration.
 *
 * Responsibilities:
 *  - secure connection orchestration
 *  - handshake + session lifecycle
 *  - service discovery
 *  - trusted device + pairing key persistence
 */
class SecureBleManagerImpl(
    private val secureClient: SecureBleGattClient,
    private val trustedDevices: TrustedDeviceInteractor,
    private val pairingKeys: PairingKeyInteractor
) : SecureBleManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val connectionStates =
        mutableMapOf<String, MutableStateFlow<SecureBleConnectionState>>()

    private val servicesFlows =
        mutableMapOf<String, MutableStateFlow<List<BleGattService>>>()

    override fun connect(address: String) {
        Log.d(LogTag, "connecting address=$address")

        val stateFlow = connectionStates.getOrPut(address) {
            MutableStateFlow(SecureBleConnectionState.Disconnected)
        }

        stateFlow.value = SecureBleConnectionState.Connecting

        scope.launch {
            val trusted = trustedDevices.loadAll()
            val isTrusted = trusted.any { it.address == address }

            val existingPairingKey = pairingKeys.load(address)

            // 1) Start secure connection (handshake happens inside secureClient)
            secureClient.connectSecure(address)

            // 2) Observe secure connection state
            secureClient.connectionState(address)
                .onEach { state ->
                    val mapped = when (state) {
                        BleGattConnectionState.Connecting ->
                            SecureBleConnectionState.Connecting

                        BleGattConnectionState.Connected -> {
                            if (!isTrusted)
                                trustedDevices.save(address)

                            val sessionKey = secureClient.currentSession(address)?.sessionKey
                            if (existingPairingKey == null && sessionKey != null)
                                    pairingKeys.save(address, sessionKey)

                            SecureBleConnectionState.SecureConnected
                        }

                        BleGattConnectionState.Disconnected ->
                            SecureBleConnectionState.Disconnected

                        is BleGattConnectionState.Failed ->
                            SecureBleConnectionState.Disconnected
                    }

                    stateFlow.value = mapped
                }
                .launchIn(this)

            // 3) Forward services
            secureClient.services(address)
                .onEach { services ->
                    servicesFlows.getOrPut(address) {
                        MutableStateFlow(emptyList())
                    }.value = services
                }
                .launchIn(this)

            Log.d(LogTag, "connected address=$address")
        }
    }

    override fun disconnect(address: String) {
        Log.d(LogTag, "disconnecting address=$address")

        secureClient.disconnect(address)
        connectionStates[address]?.value = SecureBleConnectionState.Disconnected

        Log.d(LogTag, "disconnected address=$address")
    }

    override fun connectionState(address: String): Flow<SecureBleConnectionState> =
        connectionStates.getOrPut(address) {
            MutableStateFlow(SecureBleConnectionState.Disconnected)
        }

    override fun services(address: String): Flow<List<BleGattService>> =
        servicesFlows.getOrPut(address) {
            MutableStateFlow(emptyList())
        }
}