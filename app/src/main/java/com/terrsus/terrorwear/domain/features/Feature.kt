package com.terrsus.terrorwear.domain.features

/**
 * High‑level capabilities that the app can activate or deactivate.
 *
 * These are NOT implementations — they are domain‑level flags describing
 * which hardware or subsystems a screen requires. Navigation → FeatureLifecycleController
 * uses these to start/stop the correct managers (BLE, WiFi, Sensors, etc.).
 *
 * Only include features that:
 * - require explicit opt‑in
 * - consume battery
 * - activate hardware
 * - have a lifecycle that must be started/stopped
 * - or require permissions
 */
enum class Feature {

    /** Bluetooth Low Energy scanning, connecting, GATT operations */
    BLE,

    /** WiFi networking, UDP/TCP tools, ping, etc. */
    WIFI,

    /**
     * Core motion sensors: rotation vector, accelerometer, gyroscope,
     * tap detection, gravity, linear acceleration.
     *
     * This is the main IMU bundle used by Tilt, IMU debug, compass, etc.
     */
    SENSORS,

    /**
     * Optical heart rate sensor, including:
     * - HR (BPM)
     * - HRV (if supported)
     * - SpO₂ (blood oxygen)
     *
     * These sensors are power‑hungry and should only run when needed.
     */
    HEART_RATE,

    /**
     * GPS / GNSS subsystem:
     * - GPS
     * - GLONASS
     * - Galileo
     * - BeiDou
     *
     * The Galaxy Watch 5 Pro has dual‑band GNSS and is battery‑intensive.
     */
    LOCATION,

    /**
     * Microphone access for:
     * - voice input
     * - audio recording
     * - sound‑based tools
     *
     * Requires explicit permission and should be opt‑in.
     */
    MICROPHONE,

    /**
     * Haptic feedback subsystem (vibration motor).
     *
     * Useful for games, alerts, and tactile feedback.
     * Centralizing this allows you to disable haptics globally when not needed.
     */
    HAPTICS,

    /**
     * Barometer / pressure sensor.
     *
     * Used for:
     * - elevation estimation
     * - weather pressure trends
     * - vertical movement detection
     */
    BAROMETER,

    /**
     * Magnetometer (compass sensor).
     *
     * Used for:
     * - heading / bearing
     * - compass tools
     * - orientation stabilization
     *
     * Note: rotation vector already fuses magnetometer + gyro + accel,
     * but raw magnetometer access is sometimes needed.
     */
    MAGNETOMETER,

    /**
     * Skin temperature sensor.
     *
     * The Galaxy Watch 5 Pro includes a temperature sensor, but access
     * may be limited depending on API level and Samsung Health SDK.
     */
    TEMPERATURE,

    /**
     * Foreground service or long‑running background worker.
     *
     * Required for:
     * - persistent BLE scanning
     * - long‑running WiFi servers
     * - continuous sensor logging
     * - background telemetry
     */
    FOREGROUND_SERVICE
}