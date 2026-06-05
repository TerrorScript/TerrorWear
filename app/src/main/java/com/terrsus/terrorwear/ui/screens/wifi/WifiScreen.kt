package com.terrsus.terrorwear.ui.screens.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.viewmodel.wifi.WifiViewModel

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
