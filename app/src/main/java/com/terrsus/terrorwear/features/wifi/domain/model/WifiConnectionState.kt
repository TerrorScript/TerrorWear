package com.terrsus.terrorwear.features.wifi.domain.model

/**
 * Represents the high‑level connection state of a Wi‑Fi transport component.
 *
 * This state is emitted by higher‑level controllers (e.g., WifiManager)
 * and consumed by ViewModels to update UI.
 *
 * It does not represent low‑level socket errors directly; those are surfaced
 * through [WifiEvent].
 */
sealed class WifiConnectionState {

    /** No active connection or listener. */
    data object Idle : WifiConnectionState()

    /** A TCP client is attempting to connect to a remote host. */
    data class Connecting(val host: String, val port: Int) : WifiConnectionState()

    /** A TCP client is connected to a remote host. */
    data class Connected(val host: String, val port: Int) : WifiConnectionState()

    /** A TCP server is listening for incoming clients. */
    data class Listening(val port: Int) : WifiConnectionState()

    /** A TCP server has accepted a client connection. */
    data class ClientConnected(val clientAddress: String?, val port: Int) : WifiConnectionState()

    /** The connection or listener has been intentionally closed. */
    data object Closed : WifiConnectionState()

    /**
     * A failure occurred that prevents normal operation.
     *
     * This is a high‑level error (e.g., "connection refused", "port in use").
     * Low‑level exceptions are carried in [cause] for debugging.
     */
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : WifiConnectionState()
}