package com.terrsus.terrorwear.features.wifi.domain.model

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket

/**
 * Represents discrete events emitted by Wi‑Fi transport components.
 *
 * Unlike [WifiConnectionState], which describes long‑lived state,
 * events represent instantaneous occurrences such as packet arrival,
 * connection attempts, or errors.
 */
sealed class WifiEvent {

    /** A packet was received from any Wi‑Fi transport. */
    data class Packet(val packet: WifiPacket) : WifiEvent()

    /** A TCP client successfully connected to a remote host. */
    data class Connected(val host: String, val port: Int) : WifiEvent()

    /** A TCP server accepted a client connection. */
    data class ClientAccepted(val clientAddress: String?, val port: Int) : WifiEvent()

    /** A connection or listener was closed. */
    data class Closed(val reason: String? = null) : WifiEvent()

    /**
     * A recoverable or unrecoverable error occurred.
     *
     * This is emitted by transport layers when exceptions occur.
     */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : WifiEvent()
}