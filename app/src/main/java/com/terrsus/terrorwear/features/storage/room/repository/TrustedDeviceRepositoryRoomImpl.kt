package com.terrsus.terrorwear.features.storage.room.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredTrustedDevice
import com.terrsus.terrorwear.features.storage.domain.repository.TrustedDeviceRepository
import com.terrsus.terrorwear.features.storage.room.entity.TrustedDeviceEntity
import com.terrsus.terrorwear.features.storage.room.dao.TrustedDeviceDao

/**
 * Room-backed implementation of [TrustedDeviceRepository].
 */
class TrustedDeviceRepositoryRoomImpl(
    private val dao: TrustedDeviceDao
) : TrustedDeviceRepository {

    override suspend fun loadAll(): List<StoredTrustedDevice> {
        return dao.loadAll().map { it.toDomain() }
    }

    override suspend fun save(device: StoredTrustedDevice) {
        dao.save(device.toEntity())
    }
}

private fun TrustedDeviceEntity.toDomain() = StoredTrustedDevice(
    address = address,
    name = name,
    lastSeenEpochMillis = lastSeenEpochMillis
)

private fun StoredTrustedDevice.toEntity() = TrustedDeviceEntity(
    address = address,
    name = name,
    lastSeenEpochMillis = lastSeenEpochMillis
)