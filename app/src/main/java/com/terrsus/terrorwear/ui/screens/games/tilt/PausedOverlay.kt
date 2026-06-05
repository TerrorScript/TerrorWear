package com.terrsus.terrorwear.ui.screens.games.tilt

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText


/**
 * Pause overlay with resume and restart.
 *
 * @param onResume called when the user wants to continue
 * @param onRestart called when the user wants to restart the game
 */
/**
 * Pause overlay with resume and restart.
 *
 * @param onResume called when the user wants to continue
 * @param onRestart called when the user wants to restart the game
 */
@Composable
fun PauseOverlay(
    onResume: () -> Unit,
    onRestart: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Paused")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onResume,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(30.dp)
            ) {
                Text("Resume")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onRestart,
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
}
