package com.terrsus.terrorwear.features.storage.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "highscore")
data class HighscoreEntity(
    @PrimaryKey val id: Int = 0, // always 0
    val score: Int,
    val timestamp: Long
)
