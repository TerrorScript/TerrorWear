package com.terrsus.terrorwear.features.storage.room.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredHighscore
import com.terrsus.terrorwear.features.storage.domain.repository.HighscoreRepository
import com.terrsus.terrorwear.features.storage.room.entity.HighscoreEntity
import com.terrsus.terrorwear.features.storage.room.dao.HighscoreDao

/**
 * Room-backed implementation of [HighscoreRepository].
 */
class HighscoreRepositoryRoomImpl(
    private val dao: HighscoreDao
) : HighscoreRepository {

    override suspend fun load(): StoredHighscore? {
        return dao.load()?.toDomain()
    }

    override suspend fun save(score: StoredHighscore) {
        dao.save(score.toEntity())
    }
}

private fun HighscoreEntity.toDomain() = StoredHighscore(
    id = id.toLong(),
    score = score,
    timestamp = timestamp
)

private fun StoredHighscore.toEntity() = HighscoreEntity(
    id = id.toInt(),
    score = score,
    timestamp = timestamp
)