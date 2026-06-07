package com.terrsus.terrorwear.features.storage.room.database

import android.content.Context
import androidx.room.Room

object DatabaseFactory {
    fun create(context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "terrorwear.db"
        )
            .fallbackToDestructiveMigration()
            .build()
}