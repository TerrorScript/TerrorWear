package com.terrsus.terrorwear.util

import android.os.Build

object DeviceUtils {
    fun isEmulator(): Boolean {
        val fingerprint = Build.FINGERPRINT.lowercase()
        val model = Build.MODEL.lowercase()
        val product = Build.PRODUCT.lowercase()
        val manufacturer = Build.MANUFACTURER.lowercase()
        val brand = Build.BRAND.lowercase()

        return when {
            fingerprint.contains("generic") -> true
            fingerprint.contains("emulator") -> true
            fingerprint.contains("sdk_gphone") -> true
            model.contains("emulator") -> true
            model.contains("sdk") -> true
            product.contains("sdk") -> true
            product.contains("emulator") -> true
            brand.contains("generic") -> true
            manufacturer.contains("google") && product.contains("sdk") -> true
            System.getProperty("ro.boot.qemu") == "1" -> true
            else -> false
        }
    }
}