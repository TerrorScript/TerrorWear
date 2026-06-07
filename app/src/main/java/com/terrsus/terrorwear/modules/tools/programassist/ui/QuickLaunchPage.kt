package com.terrsus.terrorwear.modules.tools.programassist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.ui.components.InfoIsland

/**
 * Quick-launch island page.
 *
 * Displays a list of frequently used desktop applications as buttons.
 * Intended to be wired to a companion channel that triggers launches on
 * the paired laptop or desktop.
 */
@Composable
fun QuickLaunchPage() {
    val programs = listOf("Discord", "Steam", "Roblox Studio")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        InfoIsland {
            Text("Quick Launch", style = MaterialTheme.typography.caption1)
            Spacer(Modifier.height(6.dp))

            programs.forEach { program ->
                Button(
                    onClick = { /* TODO: trigger launch on host */ },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(program)
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}