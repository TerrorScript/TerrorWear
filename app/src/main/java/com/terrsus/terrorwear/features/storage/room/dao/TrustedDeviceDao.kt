package com.terrsus.terrorwear.features.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.terrsus.terrorwear.features.storage.room.entity.TrustedDeviceEntity

@Dao
interface TrustedDeviceDao {

    @Query("SELECT * FROM trusted_devices")
    suspend fun loadAll(): List<TrustedDeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: TrustedDeviceEntity)
}
