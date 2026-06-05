package com.terrsus.terrorwear.ui.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.terrsus.terrorwear.ui.screens.dashboard.model.DashboardButton
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
 * @param buttons List of dashboard buttons.
 * @param onNavigate Called when a button is pressed.
 * @param viewModel DashboardViewModel instance.
 */
@Composable
fun DashboardScreen(
    buttons: List<DashboardButton>,
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
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(R.string.dashboard_title),
                    style = MaterialTheme.typography.title2,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }

            items(buttons.size) { index ->
                val button = buttons[index]

                // Fade icon based on scroll position
                val fade = remember {
                    derivedStateOf {
                        val center = listState.centerItemIndex
                        val dist = kotlin.math.abs(center - index)
                        (1f - dist * 0.4f).coerceIn(0f, 1f)
                    }
                }

                Chip(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(vertical = 4.dp),
                    label = {
                        Text(
                            text = button.label,
                            maxLines = 1
                        )
                    },
                    icon = {
                        // You don't have icons yet — fade logic still works
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Placeholder circle that fades
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = Color.White.copy(alpha = fade.value)
                                )
                            }
                        }
                    },
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