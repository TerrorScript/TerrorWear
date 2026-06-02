package com.terrsus.terrorwear.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.TerrorWearApp
import com.terrsus.terrorwear.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onNavigateStratagem: () -> Unit = {},
    onNavigateArduino: () -> Unit = {},
    onNavigateProgramAssist: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    Box(Modifier.fillMaxSize()) {
        TimeText(Modifier.align(Alignment.TopCenter))

        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
//                    contentPadding = PaddingValues(0.dp)
        ) {
            item {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                }
            }

            item {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label = { Text("Stratagem") },
                    onClick = onNavigateStratagem,
                    colors = ChipDefaults.primaryChipColors()
                )
            }

            item {
                Chip(
                    label = { Text("Arduino") },
                    onClick = onNavigateArduino,
                    colors = ChipDefaults.primaryChipColors()
                )
            }

            item {
                Chip(
                    label = { Text("Program Assist") },
                    onClick = onNavigateProgramAssist,
                    colors = ChipDefaults.primaryChipColors()
                )
            }
        }
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DashboardPreview() {
    TerrorWearApp()
}