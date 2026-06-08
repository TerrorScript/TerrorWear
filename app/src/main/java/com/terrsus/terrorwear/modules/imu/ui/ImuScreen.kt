package com.terrsus.terrorwear.modules.imu.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.modules.imu.viewmodel.ImuViewModel

/**
 * Displays live IMU sensor data.
 *
 * Shows raw accelerometer, gyroscope, magnetometer values,
 * and the fused orientation angles.
 */
@Composable
fun ImuScreen(
    viewModel: ImuViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()

    val imuRows = listOf(
        "Accel" to state.value.accel,
        "Gyro" to state.value.gyro,
        "Mag" to state.value.mag,
        "Orientation" to state.value.orientation
    )

    ScalingLazyColumn(
        modifier = Modifier,
        autoCentering = AutoCenteringParams(itemIndex = 0)
    ) {
        item {
            Text(text = "IMU")
        }

        items(imuRows) { (label, triple) ->
            Text("$label: ${triple.first}, ${triple.second}, ${triple.third}")
        }
    }
}