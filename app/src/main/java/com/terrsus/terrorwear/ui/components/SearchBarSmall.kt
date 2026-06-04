package com.terrsus.terrorwear.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun SearchBarSmall(
    query: String,
    onQueryChange: (String) -> Unit,
    deviceCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$deviceCount devices",
            style = MaterialTheme.typography.caption1
        )

        CompactChip(
            label = { Text("Search") },
            onClick = { /* expand search UI */ }
        )
    }

    // Expanded search field (optional)
    if (query.isNotEmpty()) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Filter") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
