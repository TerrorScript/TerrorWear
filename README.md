[![YouTube](https://img.shields.io/badge/YouTube-Channel-red?logo=youtube)](https://www.youtube.com/@terrsus)
![Platform](https://img.shields.io/badge/Wear%20OS-3.5-blue?logo=wearos)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple?logo=kotlin)
![Compose](https://img.shields.io/badge/Compose-Wear_OS-4285F4?logo=jetpackcompose)
![License](https://img.shields.io/badge/License-MIT-green)

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
  - There is no per-feature DI graph, compared to a tool like Hilt
</details>

## Modes (Overview)
- Tools
  - [Program Assist](#program-assist)
  - [Camera Remote](#camera-remote)
  - [Compass](#compass)
- Games
  - [Pong](#pong)
  - [Table Tilt](#table-tilt)
  - [HD2 Stratagem](#hd2-stratagem)
- Debugging
  - [Bluetooth LE](#bluetooth-le)
  - [Wi-Fi](#wifi)
  - [Inertial Measurement Unit](#inertial-measurement-unit)

## Status

Actively working on this. Having fun making new modes and redesigning the UI. :)

_Last updated: 08/06/2026_

## Installation
1. The target device must have developer settings enabled.
2. Load this project in Android Studio.
3. Pair your device to Android Studio.
4. Build the project, this will automatically install it on your device.
5. ???
6. Profit.

## Screenshots
<p>
  <img src="screenshots/dashboard.png" width="200"/>
  <img src="screenshots/program_assist.png" width="200"/>
  <img src="screenshots/pong.png" width="200"/>
</p>

## App diagram
```
AppContainer
├── Data Layer
│   ├── Room
│   ├── DataStore
│   └── BLE / Wi‑Fi stacks
├── Feature Modules
│   ├── Program Assist
│   ├── Camera Remote
│   ├── Compass
│   ├── Games
│   │   ├── Pong
│   │   ├── Table Tilt
│   │   └── HD2 Stratagem
│   └── Debug Tools
│       ├── Bluetooth LE
│       ├── Wi‑Fi
│       └── IMU
└── UI Layer (Compose for Wear OS)
    ├── Navigation
    ├── ScalingLazyColumn
    ├── Chips / Toggles
    └── Haptics
```
---
# Deep dive

## Modes

### Program Assist
A multi‑purpose companion mode for interacting with your laptop.
Handles BLE/Wi‑Fi session pairing, gesture input, and remote actions.
The presenter feature will live here since it assumes an active desktop session.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Camera Remote
Controls the paired phone’s camera.
Supports shutter, preview toggles, and basic remote actions over BLE/Wi‑Fi.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Compass
Simple directional tool that uses the magnetometer and IMU fusion.
Neat for quick orientation checks.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Pong
A little minimalist wrist‑controlled Pong.
It uses swipe gestures (in the future tilt input aswell), and haptics for feedback.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Table Tilt
A physics‑based tilt game using accelerometer data.
You move the ball by rotating your wrist, and try to survive for as long as possible whilst dodging obstacles.

### HD2 Stratagem
A Wear OS adaptation of the Helldivers 2 stratagem input system.
Pair to your desktop, then use directional gesture sequences to input stratagem codes.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Bluetooth LE
Raw GATT inspector and packet monitor.
Useful for testing your custom BLE stack.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Wi-Fi
Simple UDP/TCP test utilities for cross-device communication experiments.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

### Inertial Measurement Unit
The IMU exposes accelerometer, gyroscope, and magnetometer data. With sensor fusion, this produces a (mostly) stable 3D orientation reference.

This debugging module shows a live IMU readout. I use it to test sensor accuracy and to debug gesture input, orientation tracking, and fusion behavior.

<details>
<summary>
Screenshots
</summary>
<p>
  <img src="screenshots/dashboard.png" width="200"/>
</p>
</details>

## The Bluetooth Low Energy stack

---

## Licensing
Copyright TerrorScript © 2026

LICENSE: CC BY-NC 4.0

This work is licensed under the Creative Commons Attribution–NonCommercial 4.0 International License (CC BY‑NC 4.0).

You are free to:
- Share - copy and redistribute the material in any medium or format
- Adapt - remix, transform, and build upon the material

The licensor cannot revoke these freedoms as long as you follow the license terms.
Under the following terms:
- Attribution - You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
- NonCommercial - You may not use the material for commercial purposes.
- No additional restrictions - You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.

To view a copy of this license, visit: https://creativecommons.org/licenses/by-nc/4.0/