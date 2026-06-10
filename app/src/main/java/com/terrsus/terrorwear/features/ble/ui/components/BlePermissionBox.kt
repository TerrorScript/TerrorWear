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
fun PermissionBox(
    permissions: List<String>,
    rationale: @Composable () -> Unit = { Text("Grant permissions") },
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    var granted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        granted = result.values.all { it }
    }

    LaunchedEffect(Unit) {
        val alreadyGranted = permissions.all { perm ->
            ActivityCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (alreadyGranted) granted = true
        else launcher.launch(permissions.toTypedArray())
    }

    if (granted) content()
    else rationale()
}