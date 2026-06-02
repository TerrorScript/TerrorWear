package com.terrsus.terrorwear.tiles

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import com.terrsus.terrorwear.R

class WirelessDebugTileService : TileService() {

    private val controller by lazy { WirelessDebugController() }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    override fun onClick() {
        super.onClick()

        val enabled = controller.isWirelessDebuggingEnabled()

        if (enabled) {
            controller.disableWirelessDebugging()
        } else {
            controller.enableWirelessDebugging()
        }

        updateTile()
    }

    private fun updateTile() {
        val tile = qsTile ?: return
        val enabled = controller.isWirelessDebuggingEnabled()

        tile.state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = if (enabled) "Wireless Debug ON" else "Wireless Debug OFF"
        tile.icon = if (enabled)
            Icon.createWithResource(this, R.drawable.ic_wireless_debug_on)
        else
            Icon.createWithResource(this, R.drawable.ic_wireless_debug_off)

        tile.updateTile()
    }
}
