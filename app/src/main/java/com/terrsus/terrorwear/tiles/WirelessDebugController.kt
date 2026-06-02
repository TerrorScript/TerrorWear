package com.terrsus.terrorwear.tiles

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class WirelessDebugController {

    fun isWirelessDebuggingEnabled(): Boolean {
        val port = getProp("service.adb.tcp.port")
        return port == "5555"
    }

    fun enableWirelessDebugging() {
        Log.i("WirelessDebug", "Enabling wireless debugging")
        runShell("setprop service.adb.tcp.port 5555")
        restartAdb()
    }

    fun disableWirelessDebugging() {
        Log.i("WirelessDebug", "Disabling wireless debugging")
        runShell("setprop service.adb.tcp.port -1")
        restartAdb()
    }

    private fun restartAdb() {
        runShell("stop adbd")
        runShell("start adbd")
    }

    private fun getProp(name: String): String {
        return try {
            val process = Runtime.getRuntime().exec("getprop $name")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.readLine()?.trim() ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun runShell(cmd: String) {
        try {
            Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
        } catch (e: Exception) {
            Log.e("WirelessDebug", "Shell error: $cmd", e)
        }
    }
}
