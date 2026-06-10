package com.terrsus.terrorwear.features.wifi.domain.model

/**
 * Represents the high‑level connection or listener state of a Wi‑Fi transport component.
 *
 * Unlike [WifiEvent], which models instantaneous occurrences, this sealed class describes
 * long‑lived operational states such as “connecting”, “connected”, or “listening”.
 * These states are produced by higher‑level controllers (e.g., WifiManager) based on
 * transport‑level events and are consumed by ViewModels and UI layers to reflect the
 * current Wi‑Fi status.
 *
 * This model intentionally abstracts away low‑level socket details. Transport‑level
 * exceptions and transient conditions are surfaced through [WifiEvent.Error] rather
 * than encoded directly into connection state.
 */
sealed class WifiConnectionState {

    /**
     * No active Wi‑Fi transport is running.
     *
     * This is the default state when no TCP client, TCP server, or UDP listener
     * is active. It represents a fully idle subsystem.
     */
    data object Idle : WifiConnectionState()

    /**
     * A TCP client is attempting to establish an outbound connection.
     *
     * This state persists until the client either succeeds (transitioning to
     * [Connected]) or fails (transitioning to [Error]).
     *
     * @param host the remote host the client is attempting to connect to
     * @param port the remote TCP port
     */
    data class Connecting(val host: String, val port: Int) : WifiConnectionState()

    /**
     * A TCP client has successfully connected to a remote host.
     *
     * This state indicates that the underlying socket is open and ready for
     * bidirectional communication.
     *
     * @param host the remote host the client is connected to
     * @param port the remote TCP port
     */
    data class Connected(val host: String, val port: Int) : WifiConnectionState()

    /**
     * A transport component is actively listening for incoming traffic.
     *
     * This state is used by:
     * - TCP servers after binding to a local port and before accepting a client
     * - UDP listeners after binding to their listening port
     *
     * @param port the local port on which the component is listening
     */
    data class Listening(val port: Int) : WifiConnectionState()

    /**
     * A TCP server has accepted an inbound client connection.
     *
     * This state indicates that a remote peer is connected and that the server
     * is now receiving data from that client.
     *
     * @param clientAddress the remote client's IP address, if available
     * @param port the remote client's TCP port
     */
    data class ClientConnected(val clientAddress: String?, val port: Int) : WifiConnectionState()

    /**
     * The active connection or listener has been intentionally closed.
     *
     * This state is used when a transport component is shut down cleanly via
     * higher‑level control (e.g., WifiManager.stopTcpClient()).
     */
    data object Closed : WifiConnectionState()

    /**
     * A failure occurred that prevents normal operation.
     *
     * This represents a high‑level error condition such as:
     * - connection refused
     * - port already in use
     * - unexpected socket closure
     *
     * Low‑level exceptions are included in [cause] for debugging and logging.
     *
     * @param message a human‑readable description of the failure
     * @param cause the underlying exception, if available
     */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : WifiConnectionState()
}
