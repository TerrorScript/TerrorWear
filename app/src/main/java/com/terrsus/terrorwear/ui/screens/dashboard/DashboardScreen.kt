package com.terrsus.terrorwear.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.*
import androidx.wear.tooling.preview.devices.WearDevices
import com.terrsus.terrorwear.R
import com.terrsus.terrorwear.TerrorWearApp
import com.terrsus.terrorwear.ui.components.BackgroundGradient
import com.terrsus.terrorwear.ui.navigation.Route
import com.terrsus.terrorwear.viewmodel.dashboard.DashboardViewModel

/**
 * Main dashboard screen for TerrorWear.
 *
 * Shows a list of navigation chips with:
 * - radial gradient background
 * - Wear OS scaffold (TimeText, Vignette, PositionIndicator)
 * - consistent chip widths
 * - icon fade animation based on scroll position
 *
 * @param routes List of dashboard buttons.
 * @param onNavigate Called when a button is pressed.
 * @param viewModel DashboardViewModel instance.
 */
@Composable
fun DashboardScreen(
    routes: List<Route>,
    onNavigate: (route: String) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(VignettePosition.TopAndBottom) },
        positionIndicator = { PositionIndicator(listState) }
    ) {
        BackgroundGradient()

        ScalingLazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "TerrorWear",
                    style = MaterialTheme.typography.title1,
                    color = MaterialTheme.colors.primary,
                )
                Spacer(modifier = Modifier.height(60.dp))
            }

            items(routes.size) { index ->
                val route = routes[index]

                val tint = colorResource(route.type.colorRes)
                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = tint.copy(alpha = 0.2f),
                    ),
                    border = ChipDefaults.chipBorder(
                        borderStroke = BorderStroke(1.dp, tint),
                    ),
                    label = {
                        Text(
                            text = route.name,
                            maxLines = 1
                        )
                    },
                    secondaryLabel = {
                        Text(
                            text = route.summary
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(route.icon),
                            contentDescription = route.name,
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = { onNavigate(route.path) }
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