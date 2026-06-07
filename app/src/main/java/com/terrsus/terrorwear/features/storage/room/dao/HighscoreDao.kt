package com.terrsus.terrorwear.features.storage.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.terrsus.terrorwear.features.storage.room.entity.HighscoreEntity

@Dao
interface HighscoreDao {

    @Query("SELECT * FROM highscore WHERE id = 0")
    suspend fun load(): HighscoreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: HighscoreEntity)
}