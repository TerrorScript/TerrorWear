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
import com.terrsus.terrorwear.domain.wifi.model.WifiPacket
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiNetworkInfoViewModel
import com.terrsus.terrorwear.modules.wifi.viewmodel.WifiPacketViewModel
import com.terrsus.terrorwear.ui.navigation.Route

@Composable
fun WifiLogScreen(
    navController: NavHostController,
    viewModel: WifiPacketViewModel
) {
    val scroll = rememberScalingLazyListState()

    val allPackets by viewModel.allPackets.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(scroll) }
    ) {
        ScalingLazyColumn(
            state = scroll,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {

            // ---------------------------------------------------------
            // Title
            // ---------------------------------------------------------
            item {
                Text(
                    text = "Packet Log",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            items(allPackets.size) { index ->
                val p = allPackets[index]
                PacketRow(
                    protocol = p.from.uppercase(),
                    data = p.data.decodeToString(),
                    port = p.port,
                    timestamp = p.timestamp
                )
            }

            // ---------------------------------------------------------
            // Navigation
            // ---------------------------------------------------------
            item {
                Chip(
                    label = { Text("Back to Tools") },
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