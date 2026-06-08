package com.terrsus.terrorwear.modules.games.stratagem.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.terrsus.terrorwear.modules.games.stratagem.viewmodel.StratagemViewModel

@Composable
fun StratagemScreen(
     viewModel: StratagemViewModel
) {
    TimeText()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Stratagem (stub)")
    }
}
