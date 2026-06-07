package com.terrsus.terrorwear.features.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trusted_devices")
data class TrustedDeviceEntity(
    @PrimaryKey val address: String,
    val name: String?,
    val lastSeenEpochMillis: Long
)
