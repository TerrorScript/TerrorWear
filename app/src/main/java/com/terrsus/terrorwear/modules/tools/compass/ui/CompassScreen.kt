package com.terrsus.terrorwear.modules.tools.compass.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.modules.tools.compass.viewmodel.CompassViewModel

/**
 * Displays the current compass heading.
 *
 * The UI is intentionally simple: a centered heading readout.
 * A proper compass dial will be added later.
 */
@Composable
fun CompassScreen(
    viewModel: CompassViewModel = viewModel()
) {
    val heading = viewModel.heading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${heading.value.toInt()}°",
            textAlign = TextAlign.Center
        )
    }
}