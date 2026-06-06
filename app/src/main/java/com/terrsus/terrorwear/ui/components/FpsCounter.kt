package com.terrsus.terrorwear.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun FpsCounter(
    modifier: Modifier = Modifier,
    sampleWindowMs: Long = 500L
) {
    var fps by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        var lastTime = System.nanoTime()
        var frames = 0

        while (true) {
            frames++
            val now = System.nanoTime()
            val elapsedMs = (now - lastTime) / 1_000_000

            if (elapsedMs >= sampleWindowMs) {
                fps = (frames * 1000 / elapsedMs).toInt()
                frames = 0
                lastTime = now
            }

            // Yield to avoid blocking the main thread
            withFrameNanos { }
        }
    }

    Text(
        text = "FPS: $fps",
        color = Color.White,
        style = MaterialTheme.typography.caption1,
        modifier = modifier
    )
}
