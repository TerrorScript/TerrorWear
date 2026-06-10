package com.terrsus.terrorwear.ui.screens.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.terrsus.terrorwear.modules.dashboard.viewmodel.DashboardViewModel
import com.terrsus.terrorwear.ui.components.BackgroundGradient
import com.terrsus.terrorwear.ui.navigation.Route

/**
 * Main dashboard screen for TerrorWear.
 *
 * Displays a scrollable list of navigation chips with:
 * - a radial gradient background
 * - Wear OS scaffold elements (TimeText, Vignette, PositionIndicator)
 * - consistent chip sizing and spacing
 * - optional haptic feedback on interaction
 *
 * @param routes List of dashboard entries to display.
 * @param onNavigate Callback invoked when a route is selected.
 * @param viewModel DashboardViewModel providing UI state.
 */
@Composable
fun DashboardScreen(
    routes: List<Route>,
    onNavigate: (route: Route) -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val listState = rememberScalingLazyListState()
    val haptics = LocalHapticFeedback.current

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
                        if (route.summary != null)
                            Text(route.summary)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(route.icon),
                            contentDescription = route.name,
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNavigate(route)
                    }
                )
            }
        }
    }
}