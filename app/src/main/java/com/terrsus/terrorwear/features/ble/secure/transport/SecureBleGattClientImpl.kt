package com.terrsus.terrorwear.features.ble.secure.transport

import android.util.Log
import com.terrsus.terrorwear.features.ble.common.model.BleGattConnectionState
import com.terrsus.terrorwear.features.ble.common.model.BleGattCharacteristicValue
import com.terrsus.terrorwear.features.ble.common.model.BleGattService
import com.terrsus.terrorwear.features.ble.insecure.transport.BleGattClient
import com.terrsus.terrorwear.features.ble.secure.crypto.BleCrypto
import com.terrsus.terrorwear.features.ble.secure.handshake.HandshakeProtocol
import com.terrsus.terrorwear.features.ble.secure.session.BleSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

private const val LogTag = "TW/BLE/SecureBleGattClient"

/**
 * Secure wrapper around the insecure BleGattClient.
 * Handles:
 *  - handshake
 *  - session establishment
 *  - encryption/decryption
 *  - secure notifications
 */
class SecureBleGattClientImpl(
    private val insecure: BleGattClient,
    private val handshake: HandshakeProtocol,
    private val crypto: BleCrypto
) : SecureBleGattClient {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** Active secure sessions per device address. */
    private val sessions = mutableMapOf<String, BleSession>()

    /** Secure connection state mirrors insecure state but waits for handshake. */
    private val secureConnectionState =
        mutableMapOf<String, MutableStateFlow<BleGattConnectionState>>()

    /** Secure notifications (decrypted). */
    private val secureNotificationsFlows =
        mutableMapOf<String, MutableStateFlow<BleGattCharacteristicValue>>()

    /** Secure services (only available after handshake). */
    private val secureServicesFlows =
        mutableMapOf<String, MutableStateFlow<List<BleGattService>>>()

    override fun connectSecure(address: String) {
        Log.d(LogTag, "Secure connecting $address")

        val stateFlow = secureConnectionState.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

        Log.d(LogTag, "connecting step 1")

        // Step 1: Connect insecurely
        insecure.connect(address)

        Log.d(LogTag, "connecting step 2")

        // Step 2: Observe insecure connection state
        insecure.connectionState(address)
            .onEach { state ->
                when (state) {
                    BleGattConnectionState.Connected -> {
                        // Step 3: Perform handshake
                        performHandshake(address)
                    }
                    BleGattConnectionState.Disconnected -> {
                        sessions.remove(address)
                        stateFlow.value = BleGattConnectionState.Disconnected
                    }
                    else -> stateFlow.value = state
                }
            }
            .launchIn(scope)

        Log.d(LogTag, "Secure connected")
    }

    override fun currentSession(address: String): BleSession? =
        sessions[address]

    private fun performHandshake(address: String) {
        Log.d(LogTag, "Handshake performing address=$address")

        scope.launch {
            try {
                val session = handshake.performHandshake(address, insecure, crypto)
                sessions[address] = session

                // Mark secure connection as established
                secureConnectionState[address]?.value = BleGattConnectionState.Connected

                // Forward services from insecure client
                insecure.services(address)
                    .onEach { services ->
                        secureServicesFlows.getOrPut(address) {
                            MutableStateFlow(emptyList())
                        }.value = services
                    }
                    .launchIn(scope)

                // Forward notifications (decrypt)
                insecure.notifications(address)
                    .onEach { encrypted ->
                        val sessionKey = sessions[address]?.sessionKey ?: return@onEach
                        val plaintext = crypto.decrypt(sessionKey, encrypted.value)

                        secureNotificationsFlows.getOrPut(address) {
                            MutableStateFlow(
                                BleGattCharacteristicValue(
                                    encrypted.serviceUuid,
                                    encrypted.characteristicUuid,
                                    ByteArray(0)
                                )
                            )
                        }.value = BleGattCharacteristicValue(
                            encrypted.serviceUuid,
                            encrypted.characteristicUuid,
                            plaintext
                        )
                    }
                    .launchIn(scope)

                Log.d(LogTag, "Handshake performed address=$address")

            } catch (e: Exception) {
                secureConnectionState[address]?.value = BleGattConnectionState.Disconnected
                Log.d(LogTag, "Handshake failed address=$address e=$e")
            }
        }
    }

    override fun disconnect(address: String) {
        Log.d(LogTag, "disconnecting address=$address")
        
        insecure.disconnect(address)
        sessions.remove(address)
        secureConnectionState[address]?.value = BleGattConnectionState.Disconnected

        Log.d(LogTag, "disconnected address=$address")
    }

    override fun connectionState(address: String): Flow<BleGattConnectionState> =
        secureConnectionState.getOrPut(address) {
            MutableStateFlow(BleGattConnectionState.Disconnected)
        }

    override fun services(address: String): Flow<List<BleGattService>> =
        secureServicesFlows.getOrPut(address) {
            MutableStateFlow(emptyList())
        }

    override fun secureNotifications(address: String): Flow<BleGattCharacteristicValue> =
        secureNotificationsFlows.getOrPut(address) {
            MutableStateFlow(
                BleGattCharacteristicValue(
                    UUID(0, 0),
                    UUID(0, 0),
                    ByteArray(0)
                )
            )
        }

    override fun secureRead(
        address: String,
        service: UUID,
        characteristic: UUID
    ): Flow<BleGattCharacteristicValue> {
        val session = sessions[address] ?: return secureNotifications(address)

        return insecure.read(address, service, characteristic)
            .map { encrypted ->
                val plaintext = crypto.decrypt(session.sessionKey, encrypted.value)
                BleGattCharacteristicValue(
                    encrypted.serviceUuid,
                    encrypted.characteristicUuid,
                    plaintext
                )
            }
    }

    override fun secureWrite(
        address: String,
        service: UUID,
        characteristic: UUID,
        plaintext: ByteArray
    ) {
        val session = sessions[address] ?: return
        val encrypted = crypto.encrypt(session.sessionKey, plaintext)
        insecure.write(address, service, characteristic, encrypted)
    }

    override fun enableSecureNotifications(
        address: String,
        service: UUID,
        characteristic: UUID
    ) {
        insecure.enableNotifications(address, service, characteristic)
    }
}
