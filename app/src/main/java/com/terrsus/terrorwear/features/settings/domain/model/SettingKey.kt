package com.terrsus.terrorwear.features.settings.domain.model

/**
 * Represents a strongly-typed key for a persisted application setting.
 *
 * Using a sealed class instead of raw strings ensures type safety and
 * discoverability across the codebase.
 */
sealed class SettingKey(val key: String) {
    data object EnableTelemetry : SettingKey("enable_telemetry")
    data object ShowFpsCounter : SettingKey("show_fps_counter")
    data object EnableHaptics : SettingKey("enable_haptics")
    // Add more settings here as needed.
}
