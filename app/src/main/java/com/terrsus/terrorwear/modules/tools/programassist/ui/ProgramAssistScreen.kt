package com.terrsus.terrorwear.modules.tools.programassist.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.modules.tools.programassist.viewmodel.ProgramAssistViewModel
import com.terrsus.terrorwear.ui.components.InfoIsland
import com.terrsus.terrorwear.ui.components.TelemetryBarRow
import com.terrsus.terrorwear.ui.components.TelemetryRing

/**
 * Program Assist screen.
 *
 * Hosts a horizontally paged tool hub for laptop integration:
 * - Telemetry overview (CPU, GPU, temperature, battery, memory, disk)
 * - Quick-launch shortcuts for common programs
 * - System-level tools (volume, brightness, media)
 * - Network tools (ping, Wi‑Fi, LAN devices)
 *
 * Uses HorizontalPager for native-feeling horizontal navigation on Wear OS.
 */
@Composable
fun ProgramAssistScreen(
    viewModel: ProgramAssistViewModel
) {
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
 * Telemetry page.
 *
 * Shows a mixed-style telemetry overview:
 * - Circular summary rings for high-level CPU, GPU, temperature, and battery state
 * - Minimalist bar gauges inside InfoIslands for detailed load metrics
 *
 * Designed for quick glances on a round Wear OS display, with haptic feedback
 * on interaction and room for future live data integration.
 */
@Composable
private fun TelemetryPage() {
    // Placeholder values; wire these to real telemetry streams later.
    val battery = 0.42f      // 42%
    val cpuLoad = 0.73f      // 73%
    val gpuLoad = 0.38f      // 38%
    val cpuTempC = 68
    val gpuTempC = 55
    val cpuTempNorm = cpuTempC / 100f
    val gpuTempNorm = gpuTempC / 100f
    val memoryLoad = 0.68f
    val diskLoad = 0.31f

    val scrollState = rememberScalingLazyListState(initialCenterItemIndex = 0)
    val haptics = LocalHapticFeedback.current

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
            // Summary rings (CPU, GPU, Temp, Battery)
            item {
                TelemetrySummaryGrid(
                    cpuLoad = cpuLoad,
                    gpuLoad = gpuLoad,
                    cpuTemp = cpuTempNorm,
                    battery = battery
                )
                Spacer(Modifier.height(12.dp))
            }

            // Load island (CPU / GPU / Memory / Disk)
            item {
                var expanded by remember { mutableStateOf(false) }

                InfoIsland(
                    modifier = Modifier
                        .clickable(
                            indication = LocalIndication.current,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            expanded = !expanded
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        },
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("System Load", style = MaterialTheme.typography.caption1)
                    Spacer(Modifier.height(6.dp))

                    TelemetryBarRow(
                        label = "CPU",
                        value = cpuLoad,
                        iconRes = R.drawable.baseline_memory_24,
                        displayText = "${(cpuLoad * 100).toInt()}%"
                    )
                    TelemetryBarRow(
                        label = "GPU",
                        value = gpuLoad,
                        iconRes = R.drawable.baseline_monitor_24,
                        displayText = "${(gpuLoad * 100).toInt()}%"
                    )
                    TelemetryBarRow(
                        label = "Memory",
                        value = memoryLoad,
                        iconRes = R.drawable.outline_memory_alt_24,
                        displayText = "${(memoryLoad * 100).toInt()}%"
                    )
                    TelemetryBarRow(
                        label = "Disk",
                        value = diskLoad,
                        iconRes = R.drawable.baseline_storage_24,
                        displayText = "${(diskLoad * 100).toInt()}%"
                    )

                    if (expanded) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Per-core and per-drive details coming soon…",
                            style = MaterialTheme.typography.caption2
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Temperature island
            item {
                InfoIsland(
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Thermals", style = MaterialTheme.typography.caption1)
                    Spacer(Modifier.height(6.dp))

                    TelemetryBarRow(
                        label = "CPU Temp",
                        value = cpuTempNorm,
                        iconRes = R.drawable.baseline_thermostat_24,
                        displayText = "${cpuTempC}°C"
                    )
                    TelemetryBarRow(
                        label = "GPU Temp",
                        value = gpuTempNorm,
                        iconRes = R.drawable.baseline_thermostat_24,
                        displayText = "${gpuTempC}°C"
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            // Future: network / latency / FPS telemetry islands

            item {
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

/**
 * Summary grid of circular telemetry rings.
 *
 * Uses a 2x2 layout to present high-level metrics:
 * - CPU load
 * - GPU load
 * - CPU temperature
 * - Battery level
 *
 * Each ring is independent and uses its own icon and color semantics.
 */
@Composable
private fun TelemetrySummaryGrid(
    cpuLoad: Float,
    gpuLoad: Float,
    cpuTemp: Float,
    battery: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TelemetryRing(
                label = "CPU",
                value = cpuLoad,
                iconRes = R.drawable.baseline_memory_24
            )
            TelemetryRing(
                label = "GPU",
                value = gpuLoad,
                iconRes = R.drawable.baseline_monitor_24
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TelemetryRing(
                label = "Temp",
                value = cpuTemp,
                iconRes = R.drawable.baseline_thermostat_24
            )
            TelemetryRing(
                label = "Batt",
                value = battery,
                iconRes = R.drawable.baseline_battery_full_24
            )
        }
    }
}