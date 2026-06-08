[![YouTube](https://img.shields.io/badge/YouTube-Channel-red?logo=youtube)](https://www.youtube.com/@terrsus)

# TerrorWear

TerrorWear is my modular Wear OS sandbox, which has small games, device tools, and experimental features like camera remote control and program assist.

I built it around my smartwatch (Samsung Galaxy Watch5 pro), because... \
...I like programming, \
...wanted to learn how to use android studio and \
...wanted to get more out of my smartwatch.

## Features
- **Modular architecture**: each feature is isolated from the rest.
- **Miniature games**: Pong, Tilt and more
- **Device tools**: camera remote, program assist, BLE utilities
- **Connectivity experiments**: BLE, Wi‑Fi, sensors, and communication with other devices
- **Using Wear OS native interface**: ScalingLazyColumn, ToggleChips, haptics, round‑screen layouts, etc.

## What it uses
- [Kotlin](https://kotlinlang.org/)
- [Compose for Wear OS](https://developer.android.com/training/wearables/compose?version=3)
- Data saving: [Room](https://developer.android.com/training/data-storage/room) and [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
- Custom BLE stack
- <details><summary>AppContainer-based DI to keep it simple</summary>
- A single DI entry point (AppContainer)
- Features pull dependencies from that one container
- Modules don’t construct dependencies themselves
- There is no per-feature DI graph, like with Hilt components
</details>

## Modes (Overview)
- Tools
  - [Program Assist](#program-assist)
  - [Camera Remote](#camera-remote)
- Games
  - [Pong](#pong)
  - [Table tilt](#table-tilt)
  - [HD2 Stratagem](#hd2-stratagem)
- Debugging
  - [Bluetooth LE](#bluetooth-le)
  - [WiFi](#wifi)
  - [Inertial Measurement Unit](#inertial-measurement-unit)

## Status

Actively working on this. Having fun making new modes and redesigning the UI :)

_Last updated: 08/06/2026_

# Deep dive
## Modes
#### Program Assist
A multi‑purpose companion mode for interacting with your laptop.
Handles BLE/Wi‑Fi session pairing, gesture input, and remote actions.
The presenter feature will live here since it assumes an active desktop session.
### Camera Remote
Controls the paired phone’s camera.
Supports shutter, preview toggles, and basic remote actions over BLE/Wi‑Fi.
### Compass
Simple directional tool using magnetometer + IMU fusion.
Useful for quick orientation checks.
### Pong
Minimalist wrist‑controlled Pong.
Uses tilt input and haptics for feedback.
### Table tilt
A physics‑based tilt game using accelerometer data.
Move the ball by rotating your wrist.
### HD2 Stratagem
A Wear OS adaptation of the Helldivers 2 stratagem input system.
Directional sequences + haptic confirmation.
### Bluetooth LE
Raw GATT inspector and packet monitor.
Useful for testing your custom BLE stack.
### WiFi
Simple UDP/TCP test utilities for cross‑device communication experiments.
### Inertial Measurement Unit
Live IMU readout (accelerometer, gyroscope, magnetometer).
Used for debugging gesture input and sensor fusion.