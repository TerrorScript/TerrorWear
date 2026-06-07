package com.terrsus.terrorwear.features.ble.secure.session

/**
 * Represents the secure BLE connection state.
 */
enum class SecureBleConnectionState {
    /** Not connected and no session exists. */
    Disconnected,

    /** Connecting insecurely + performing handshake. */
    Connecting,

    /** Secure session established and ready. */
    SecureConnected
}
