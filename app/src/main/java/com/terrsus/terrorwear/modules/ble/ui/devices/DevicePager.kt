package com.terrsus.terrorwear.ui.screens.ble

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.terrsus.terrorwear.features.ble.domain.model.BleDevice
import com.terrsus.terrorwear.ui.components.BleDeviceRow


@Composable
fun DevicePager(
    results: List<BleDevice>,
    selectedDevice: BleDevice?,
    onSelectDevice: (BleDevice) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { results.size })

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val device = results[page]
            BleDeviceRow(device, onSelectDevice)
        }
    }
}