package com.terrsus.terrorwear.features.ble.common.util

fun ByteArray.toHexString(): String =
    joinToString(" ") { "%02X".format(it) }

fun ByteArray.toPrettyString(): String {
    val hex = joinToString(" ") { "%02X".format(it) }
    val ascii = map { if (it in 32..126) it.toInt().toChar() else '.' }.joinToString("")
    return "$hex   |$ascii|"
}