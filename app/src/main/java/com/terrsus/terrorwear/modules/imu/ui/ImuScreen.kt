package com.terrsus.terrorwear.modules.imu.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.terrsus.terrorwear.AppContainer
import com.terrsus.terrorwear.ui.components.ValueGraph


/**
 * IMU debug screen.
 *
 * Shows:
 * - Raw acceleration
 * - Orientation (yaw, pitch, roll)
 * - Arc indicators around the bezel
 * - A small rolling graph of yaw changes
 */
@Composable
fun ImuScreen() {
    val sensorManager = AppContainer.sensorManager

    val orientation = sensorManager.orientation.collectAsState()
    val accel = sensorManager.acceleration.collectAsState()

    // Rolling history for angle value graph
    val maxPoints = 60
    var pitchHistory by remember { mutableStateOf(List(maxPoints) { 0f }) }
    var yawHistory by remember { mutableStateOf(List(maxPoints) { 0f }) }
    var rollHistory by remember { mutableStateOf(List(maxPoints) { 0f }) }

    LaunchedEffect(orientation.value) {
        pitchHistory = (pitchHistory + orientation.value.pitch).takeLast(maxPoints)
        yawHistory = (yawHistory + orientation.value.yaw).takeLast(maxPoints)
        rollHistory = (rollHistory + orientation.value.roll).takeLast(maxPoints)
    }

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Display orientation indicators along bezel
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 4.dp.toPx()

                // Outermost radius (touching bezel)
                val outerRadius = size.minDimension / 2f - stroke / 2

                drawArcAtRadius(
                    color = AngleColorLookup.yaw,
                    sweep = -orientation.value.yaw,
                    radius = outerRadius,
                    stroke = stroke,
                    alpha = 0.7f
                )
                drawArcAtRadius(
                    color = AngleColorLookup.pitch,
                    sweep = orientation.value.pitch,
                    radius = outerRadius - stroke * 1f,
                    stroke = stroke,
                    alpha = 0.7f
                )
                drawArcAtRadius(
                    color = AngleColorLookup.roll,
                    sweep = -orientation.value.roll,
                    radius = outerRadius - stroke * 2f,
                    stroke = stroke,
                    alpha = 0.7f
                )
            }

            // Display orientation graphs
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.4f)
                    .background(Color(100, 100, 100, 20))
                    .border(width = 1.dp, color = Color(1f, 1f, 1f, 0.3f))
                    .align(Alignment.Center),
            ) {
                ValueGraph(
                    values = rollHistory,
                    color = AngleColorLookup.roll,
                    alpha = 0.4f
                )
                ValueGraph(
                    values = pitchHistory,
                    color = AngleColorLookup.pitch,
                    alpha = 0.4f
                )
                ValueGraph(
                    values = yawHistory,
                    color = AngleColorLookup.yaw,
                    alpha = 0.4f
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .padding(6.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "IMU",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center
                )

                fun Float.format() = String.format("%.2f", this)
                Text(
                    "Accel:\nX = ${accel.value.x.format()}\nY = ${accel.value.y.format()}\nZ = ${accel.value.z.format()}",
                    fontSize = 14.sp
                )


                ImuValueBlock("Orientation") {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = AngleColorLookup.yaw)) {
                                append("Y = ${orientation.value.yaw.toInt()}°\n")
                            }
                            withStyle(SpanStyle(color = AngleColorLookup.pitch)) {
                                append("P = ${orientation.value.pitch.toInt()}°\n")
                            }
                            withStyle(SpanStyle(color = AngleColorLookup.roll)) {
                                append("R = ${orientation.value.roll.toInt()}°")
                            }
                        },
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ImuValueBlock(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.White
        )
        content()
    }
}