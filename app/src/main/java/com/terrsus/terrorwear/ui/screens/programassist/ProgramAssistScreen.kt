package com.terrsus.terrorwear.ui.screens.programassist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.ui.components.InfoIsland

/**
 * Program Assist screen.
 *
 * Uses HorizontalPager for clean, native-feeling horizontal navigation.
 * Each page displays placeholder content for telemetry, quick-launch
 * programs, and future laptop-integration tools.
 *
 * No manual gesture detection is required.
 */
@Composable
fun ProgramAssistScreen() {

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 4 })

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) { page ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text("Program Assist", style = MaterialTheme.typography.title2)
                    Spacer(Modifier.height(12.dp))

                    when (page) {
                        0 -> TelemetryPage()
                        1 -> QuickLaunchPage()
                        2 -> SystemToolsPage()
                        3 -> NetworkToolsPage()
                    }
                }
            }
        }
    }
}

/**
 * Telemetry info island page.
 */
@Composable
private fun TelemetryPage() {
    val battery = "?? %"
    val cpuTemp = "?? °C"
    val gpuTemp = "?? °C"
    val cpuLoad = "?? %"
    val gpuLoad = "?? %"
    val memoryLoad = "?? %"
    val diskLoad = "?? %"

    val scrollState = rememberScalingLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        ScalingLazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                InfoIsland {
                    Text("Battery: ?? %")
                    Text("CPU Temp: ?? °C")
                    Text("GPU Temp: ?? °C")
                }
                Spacer(Modifier.height(8.dp))
            }

            item {
                InfoIsland {
                    Text("CPU Load: ?? %")
                    Text("GPU Load: ?? %")
                    Text("Memory Load: ?? %")
                    Text("Disk Load: ?? %")
                }
                Spacer(Modifier.height(8.dp))
            }

            // Add more telemetry islands later
        }
    }
}

/**
 * Quick-launch island page.
 */
@Composable
private fun QuickLaunchPage() {
    val programs = listOf("Discord", "Steam", "Roblox Studio")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        InfoIsland {
            Text("Quick Launch", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))

            programs.forEach { program ->
                Button(
                    onClick = { /* stub */ },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(program)
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Placeholder for system-level laptop tools.
 */
@Composable
private fun SystemToolsPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoIsland {
            Text("System Tools", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))
            Text("Volume control (stub)")
            Text("Brightness control (stub)")
            Text("Media keys (stub)")
        }
    }
}

/**
 * Placeholder for network-related laptop tools.
 */
@Composable
private fun NetworkToolsPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoIsland {
            Text("Network Tools", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))
            Text("Ping laptop (stub)")
            Text("Wi-Fi strength (stub)")
            Text("LAN device list (stub)")
        }
    }
}