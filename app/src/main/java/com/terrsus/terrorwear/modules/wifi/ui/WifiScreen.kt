package com.terrsus.terrorwear.modules.wifi.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiViewModel

@Composable
fun WifiScreen(
    navController: NavHostController,
    viewModel: WifiViewModel
) {
    val udp by viewModel.udpPackets.collectAsState()
    val tcp by viewModel.tcpPackets.collectAsState()
    val status by viewModel.status.collectAsState()

    val scroll = rememberScalingLazyListState()

    Scaffold(
        positionIndicator = { PositionIndicator(scroll) },
        vignette = { Vignette(VignettePosition.TopAndBottom) }
    ) {
        ScalingLazyColumn(
            state = scroll,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text("Wi‑Fi Debug")
            }

            item {
                Chip(
                    label = { Text("Send UDP Ping") },
                    onClick = { viewModel.sendUdp("ping", "192.168.0.10", 9000) }
                )
            }

            item {
                Chip(
                    label = { Text("Send TCP Ping") },
                    onClick = { viewModel.sendTcp("ping") }
                )
            }

            item {
                Text("UDP Packets: ${udp.size}")
            }

            items(udp.size) { i ->
                Text("UDP: " + udp[i].data.decodeToString())
            }

            item {
                Text("TCP Packets: ${tcp.size}")
            }

            items(tcp.size) { i ->
                Text("TCP: " + tcp[i].data.decodeToString())
            }

            item {
                Text("Status: $status")
            }
        }
    }
}
