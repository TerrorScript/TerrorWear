package com.terrsus.terrorwear

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import com.terrsus.terrorwear.theme.TerrorWearTheme
import com.terrsus.terrorwear.ui.navigation.TerrorWearNavGraph

@Composable
fun TerrorWearApp(
    modifier: Modifier = Modifier
) {
    TerrorWearTheme {
        MaterialTheme {
            val navController = rememberNavController()
            TerrorWearNavGraph(navController)
        }
    }
}