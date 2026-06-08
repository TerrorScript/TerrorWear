package com.terrsus.terrorwear.modules.wifi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun PacketRow(
    protocol: String,
    data: String,
    port: Int,
    timestamp: Long
) {
    val color = when (protocol) {
        "UDP" -> MaterialTheme.colors.primary
        "TCP" -> MaterialTheme.colors.secondary
        "TCP-SERVER" -> MaterialTheme.colors.error // orange/red
        else -> MaterialTheme.colors.onBackground
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = protocol,
            color = color,
            style = MaterialTheme.typography.caption2,
            modifier = Modifier.width(60.dp)
        )

        Column {
            Text(
                text = data,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = "${timestamp % 100000}", // short timestamp
                style = MaterialTheme.typography.caption3
            )
        }
    }
}