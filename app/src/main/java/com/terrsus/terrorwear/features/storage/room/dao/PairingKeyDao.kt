package com.terrsus.terrorwear.features.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.terrsus.terrorwear.features.storage.room.entity.PairingKeyEntity

/**
 * DAO for long-term BLE pairing keys.
 */
@Dao
interface PairingKeyDao {

    @Query("SELECT * FROM pairing_keys WHERE address = :address LIMIT 1")
    suspend fun load(address: String): PairingKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: PairingKeyEntity)
}
