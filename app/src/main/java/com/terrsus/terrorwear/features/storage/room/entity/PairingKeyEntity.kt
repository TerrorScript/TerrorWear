package com.terrsus.terrorwear.features.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for long-term BLE pairing keys.
 */
@Entity(tableName = "pairing_keys")
data class PairingKeyEntity(
    @PrimaryKey val address: String,
    val keyBytes: ByteArray,
    val createdAt: Long
)
