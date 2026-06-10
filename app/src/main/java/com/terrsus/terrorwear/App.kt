package com.terrsus.terrorwear

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.theme.TerrorWearTheme
import com.terrsus.terrorwear.ui.components.FpsCounter
import com.terrsus.terrorwear.ui.navigation.NavGraph

@Composable
fun TerrorWearApp(
    context: Context,
    modifier: Modifier = Modifier
) {
    TerrorWearTheme {
        MaterialTheme {
            val navController = rememberNavController()
            NavGraph(
                context = context,
                navController = navController
            )
        }
    }
}