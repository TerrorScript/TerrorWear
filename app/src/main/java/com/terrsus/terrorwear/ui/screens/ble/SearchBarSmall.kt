package com.terrsus.terrorwear.ui.screens.ble

import android.app.RemoteInput
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper


@Composable
fun SearchBarSmall(
    query: String,
    onQueryChange: (String) -> Unit,
    deviceCount: Int
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val remoteInput = RemoteInput.getResultsFromIntent(result.data)
        val text = remoteInput?.getCharSequence("query")?.toString()
        if (text != null) onQueryChange(text)
    }

    fun launchTextInput() {
        val remoteInput = RemoteInput.Builder("query")
            .setLabel("Filter devices")
            .build()

        val intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
        RemoteInputIntentHelper.putRemoteInputsExtra(intent, listOf(remoteInput))

        launcher.launch(intent)
    }

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
            label = { Text(query.ifEmpty { "Search" }) },
            onClick = {
                if (query.isNotEmpty()) onQueryChange("")
                else launchTextInput()
            }
        )
    }
}