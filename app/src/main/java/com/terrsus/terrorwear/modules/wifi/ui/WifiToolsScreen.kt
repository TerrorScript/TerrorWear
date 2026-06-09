package com.terrsus.terrorwear.modules.wifi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiConnectionViewModel
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiNetworkInfoViewModel
import com.terrsus.terrorwear.ui.navigation.Route

@Composable
fun WifiToolsScreen(
    navController: NavHostController,
    viewModel: WifiConnectionViewModel
) {
    val scroll = rememberScalingLazyListState()
    val status by viewModel.status.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scroll) }
    ) {
        ScalingLazyColumn(
            state = scroll,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = "Wi‑Fi Tools",
                    style = MaterialTheme.typography.title3
                )
            }

            // ------------------------------
            // UDP Tools
            // ------------------------------
            item { SectionHeader("UDP Tools") }

            item {
                Chip(
                    label = { Text("Start UDP Listener") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.UdpStartListening),
                            contentDescription = "Start UDP"
                        )
                    },
                    onClick = { viewModel.restartUdp(9000) }
                )
            }

            item {
                Chip(
                    label = { Text("Send UDP Packet") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.UdpSendPacket),
                            contentDescription = "Send UDP"
                        )
                    },
                    onClick = { viewModel.sendUdp("ping", "192.168.0.10", 9000) }
                )
            }

            // ------------------------------
            // TCP Tools
            // ------------------------------
            item { SectionHeader("TCP Tools") }

            item {
                Chip(
                    label = { Text("Start TCP Server") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Server),
                            contentDescription = "TCP Server"
                        )
                    },
                    onClick = { viewModel.startTcpServer(9001) }
                )
            }

            item {
                Chip(
                    label = { Text("Connect TCP Client") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Client),
                            contentDescription = "TCP Client"
                        )
                    },
                    onClick = { viewModel.restartTcp("192.168.0.10", 9001) }
                )
            }

            item {
                Chip(
                    label = { Text("Send TCP Packet") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Send),
                            contentDescription = "Send TCP"
                        )
                    },
                    onClick = { viewModel.sendTcp("ping") }
                )
            }

            // ------------------------------
            // Diagnostics
            // ------------------------------
            item { SectionHeader("Diagnostics") }

            item {
                Chip(
                    label = { Text("Ping Host") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Ping),
                            contentDescription = "Ping"
                        )
                    },
                    onClick = {
                        TODO()
                    }
                )
            }

            item {
                Chip(
                    label = { Text("mDNS Discovery") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Mdns),
                            contentDescription = "mDNS"
                        )
                    },
                    onClick = {
                        TODO()
                    }
                )
            }

            item {
                Chip(
                    label = { Text("SSDP Discovery") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.Ssdp),
                            contentDescription = "SSDP"
                        )
                    },
                    onClick = {
                        TODO()
                    }
                )
            }

            item { Text("Status: $status") }

            // ------------------------------
            // Navigation
            // ------------------------------
            item {
                Chip(
                    label = { Text("Packet Log") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.List),
                            contentDescription = "Packet Log"
                        )
                    },
                    onClick = { navController.navigate(Route.WifiLogs.path) }
                )
            }

            item {
                Chip(
                    label = { Text("Back to Info") },
                    icon = {
                        Icon(
                            painter = painterResource(WifiIcons.NavigateBack),
                            contentDescription = "Back"
                        )
                    },
                    onClick = { navController.popBackStack() }
                )
            }
        }
    }
}