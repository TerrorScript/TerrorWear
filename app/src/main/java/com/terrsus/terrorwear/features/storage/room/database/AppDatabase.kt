package com.terrsus.terrorwear.features.storage.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.terrsus.terrorwear.features.storage.room.entity.HighscoreEntity
import com.terrsus.terrorwear.features.storage.room.entity.PairingKeyEntity
import com.terrsus.terrorwear.features.storage.room.entity.TrustedDeviceEntity
import com.terrsus.terrorwear.features.storage.room.dao.HighscoreDao
import com.terrsus.terrorwear.features.storage.room.dao.PairingKeyDao
import com.terrsus.terrorwear.features.storage.room.dao.TrustedDeviceDao

@Database(
    entities = [
        TrustedDeviceEntity::class,
        HighscoreEntity::class,
        PairingKeyEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trustedDeviceDao(): TrustedDeviceDao
    abstract fun highscoreDao(): HighscoreDao
    abstract fun pairingKeyDao(): PairingKeyDao

    companion object {
        fun build(context: Context): AppDatabase = DatabaseFactory.create(context)
    }
}