package com.terrsus.terrorwear.features.ble.common.model

enum class GattProperty(val bit: Int) {
    READ(0x02),
    WRITE(0x04),
    NOTIFY(0x08)
}

fun Int.toGattProperties(): List<GattProperty> {
    val result = mutableListOf<GattProperty>()

    for (property in GattProperty.entries) {
        val bitIsSet = (this and property.bit) != 0

        if (bitIsSet) {
            result.add(property)
        }
    }

    return result
}

val GattProperty.displayName: String
    get() = when (this) {
        GattProperty.READ -> "Read"
        GattProperty.WRITE -> "Write"
        GattProperty.NOTIFY -> "Notify"
    }