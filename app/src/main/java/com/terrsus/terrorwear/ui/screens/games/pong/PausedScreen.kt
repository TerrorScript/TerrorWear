package com.terrsus.terrorwear.ui.screens.games.pong

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.viewmodel.games.pong.PongViewModel

/**
 * Pause screen with resume and restart buttons.
 *
 * @param viewModel ViewModel controlling game state.
 */
@Composable
fun PausedScreen(viewModel: PongViewModel) {
    Box(
        Modifier
            .fillMaxSize()
            .blur(20.dp)
    )
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Paused")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.resume() },
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(30.dp),
        ) {
            Text("Resume")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = { viewModel.restart() },
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red
            )
        ) {
            Text("Restart")
        }
    }
}