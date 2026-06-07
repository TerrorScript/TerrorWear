package com.terrsus.terrorwear.features.storage.room.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredPairingKey
import com.terrsus.terrorwear.features.storage.domain.repository.PairingKeyRepository
import com.terrsus.terrorwear.features.storage.room.entity.PairingKeyEntity
import com.terrsus.terrorwear.features.storage.room.dao.PairingKeyDao

/**
 * Room-backed implementation of [PairingKeyRepository].
 */
class PairingKeyRepositoryRoomImpl(
    private val dao: PairingKeyDao
) : PairingKeyRepository {

    override suspend fun load(address: String): StoredPairingKey? {
        return dao.load(address)?.toDomain()
    }

    override suspend fun save(pairingKey: StoredPairingKey) {
        dao.save(pairingKey.toEntity())
    }
}

private fun PairingKeyEntity.toDomain() = StoredPairingKey(
    address = address,
    keyBytes = keyBytes,
    createdAtEpochMillis = createdAt
)

private fun StoredPairingKey.toEntity() = PairingKeyEntity(
    address = address,
    keyBytes = keyBytes,
    createdAt = createdAtEpochMillis
)