package com.terrsus.terrorwear.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.terrsus.terrorwear.domain.features.Feature
import com.terrsus.terrorwear.ui.navigation.Permission

/**
 * A global screen shown when the user denies one or more permissions required
 * for a feature to function. This screen is intentionally styled in a strong
 * red theme to emphasize that the requested feature cannot proceed.
 *
 * @param features The list of features that failed due to missing permissions.
 * @param requiredPermissions The list of Android permissions that were denied.
 * @param onBack Callback invoked when the user presses the Back button.
 */
@Composable
fun PermissionDeniedScreen(
    features: List<Feature>,
    requiredPermissions: List<Permission>,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF5A0000)) // dark bloody red
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Title — reduced size
            Text(
                text = "Permission Denied",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle — centered, reduced size
            Text(
                text = "The following feature(s) cannot be used:",
                color = Color.White,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(4.dp))

            // Feature list
            features.forEach { feature ->
                Text(
                    text = "• ${feature.name}",
                    color = Color(0xFFFFB3B3),
                    fontSize = 10.sp
                )
            }

            Spacer(Modifier.height(12.dp)) // reduced from ~36dp

            // Missing permissions header
            Text(
                text = "Missing permissions:",
                color = Color.White,
                fontSize = 10.sp
            )

            Spacer(Modifier.height(4.dp))

            // Permission list
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.98f),   // keeps text aligned
                horizontalAlignment = Alignment.Start
            ) {
                requiredPermissions.forEach { perm ->
                    Text(
                        text = "• ${perm.androidName}",
                        color = Color(0xFFFF9999),
                        fontSize = 9.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp)) // reduced from ~32dp

            // Back button
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF8A0000),
                    contentColor = Color.White
                )
            ) {
                Text("Back")
            }
        }
    }
}