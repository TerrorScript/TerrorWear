package com.terrsus.terrorwear.features.ble.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.wear.compose.material.Text
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun BlePermissionBox(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    var granted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        granted = result.values.all { it }
    }

    LaunchedEffect(Unit) {
        val alreadyGranted = permissions.all {
            ActivityCompat.checkSelfPermission(context, it) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) granted = true
        else launcher.launch(permissions)
    }

    if (granted) content()
    else Text("Grant Bluetooth permissions")
}
