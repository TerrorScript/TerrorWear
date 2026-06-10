package com.terrsus.terrorwear.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi

sealed interface Permission {
    val androidName: String

    data object BluetoothScan : Permission {
        override val androidName = android.Manifest.permission.BLUETOOTH_SCAN
    }

    data object BluetoothConnect : Permission {
        override val androidName = android.Manifest.permission.BLUETOOTH_CONNECT
    }

    data object BluetoothAdvertise : Permission {
        override val androidName = android.Manifest.permission.BLUETOOTH_ADVERTISE
    }

    data object WifiState : Permission {
        override val androidName = android.Manifest.permission.ACCESS_WIFI_STATE
    }

    data object WifiChange : Permission {
        override val androidName = android.Manifest.permission.CHANGE_WIFI_STATE
    }

    data object FineLocation : Permission {
        override val androidName = android.Manifest.permission.ACCESS_FINE_LOCATION
    }

    data object CoarseLocation : Permission {
        override val androidName = android.Manifest.permission.ACCESS_COARSE_LOCATION
    }

    data object BodySensors : Permission {
        override val androidName = android.Manifest.permission.BODY_SENSORS
    }

    data object ActivityRecognition : Permission {
        override val androidName = android.Manifest.permission.ACTIVITY_RECOGNITION
    }

    data object Microphone : Permission {
        override val androidName = android.Manifest.permission.RECORD_AUDIO
    }

    data object ForegroundService : Permission {
        override val androidName = android.Manifest.permission.FOREGROUND_SERVICE
    }
}
