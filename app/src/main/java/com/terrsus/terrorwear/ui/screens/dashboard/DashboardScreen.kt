package com.terrsus.terrorwear.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.TerrorWearApp
import com.terrsus.terrorwear.viewmodel.dashboard.DashboardViewModel

@Composable
fun DashboardScreen(
    buttons: List<DashboardButton>,
    onNavigate: (route: String) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {

    Box(Modifier.fillMaxSize()) {
        TimeText(Modifier.align(Alignment.TopCenter))

        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            items(buttons.size) { index ->
                val button = buttons[index]
                Chip(
                    label = { Text(button.label) },
                    onClick = { onNavigate(button.route) }
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