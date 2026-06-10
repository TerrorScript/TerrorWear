package com.terrsus.terrorwear.features.wifi.domain.model

import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.features.wifi.manager.WifiManager

/**
 * Represents instantaneous events emitted by Wi‑Fi transport components.
 *
 * Unlike [WifiConnectionState], which models long‑lived connection or listener
 * status, events describe discrete occurrences such as packet arrival, client
 * acceptance, connection establishment, or transport‑level failures.
 *
 * All events are forwarded through [WifiManager.events] and may be used by
 * higher‑level layers (repository, view model, UI) to react to transport
 * activity without directly depending on socket implementations.
 */
sealed class WifiEvent {

    /**
     * A raw packet was received from any Wi‑Fi transport.
     *
     * This event is emitted by:
     * - TCP clients when receiving data from a remote server
     * - TCP servers when receiving data from a connected client
     * - UDP listeners when receiving a datagram
     *
     * The [packet] contains the raw payload and metadata such as sender address
     * and port.
     */
    data class Packet(val packet: WifiPacket) : WifiEvent()

    /**
     * A TCP client successfully established an outbound connection to a remote host.
     *
     * This event is emitted only by TCP client transports. It indicates that the
     * underlying socket has connected and is ready for bidirectional communication.
     */
    data class Connected(val host: String, val port: Int) : WifiEvent()

    /**
     * A TCP server accepted an inbound client connection.
     *
     * This event is emitted only by TCP server transports. It indicates that a
     * remote peer has connected to the listening socket and that the server is now
     * receiving data from that client.
     *
     * @param clientAddress the remote client's IP address, if available
     * @param port the remote client's TCP port
     */
    data class ClientAccepted(val clientAddress: String?, val port: Int) : WifiEvent()

    /**
     * Indicates that a transport component has begun listening on the given [port].
     *
     * This event is emitted by:
     * - TCP servers after binding to a local port and before accepting clients
     * - UDP listeners after binding to their listening port
     *
     * It signals that the local socket is active and ready to receive traffic,
     * but does not imply that any remote peer has connected or sent data yet.
     */
    data class Listening(val port: Int) : WifiEvent()

    /**
     * A transport component was closed, either intentionally or due to remote
     * disconnection.
     *
     * This event is emitted by all transport types when their underlying socket
     * or listener is shut down. The optional [reason] may provide context such as
     * "remote closed", "local disconnect", or an implementation‑specific message.
     */
    data class Closed(val reason: String? = null) : WifiEvent()

    /**
     * A recoverable or unrecoverable transport‑level error occurred.
     *
     * This event is emitted when exceptions arise during connection attempts,
     * read/write operations, listener setup, or shutdown. The [message] provides
     * a human‑readable description, while [cause] contains the underlying
     * exception when available.
     */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : WifiEvent()
}
