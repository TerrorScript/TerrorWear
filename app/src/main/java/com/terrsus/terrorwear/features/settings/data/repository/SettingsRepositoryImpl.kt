package com.terrsus.terrorwear.features.settings.data.repository

import com.terrsus.terrorwear.features.settings.domain.model.SettingKey
import com.terrsus.terrorwear.features.settings.domain.model.SettingValue
import com.terrsus.terrorwear.features.settings.domain.repository.SettingsRepository
import com.terrsus.terrorwear.features.settings.data.datastore.SettingsStore
import kotlinx.coroutines.flow.firstOrNull

/**
 * DataStore-backed implementation of [SettingsRepository].
 *
 * Converts between domain-level models and the underlying key-value
 * storage format used by DataStore.
 */
class SettingsRepositoryImpl(
    private val store: SettingsStore
) : SettingsRepository {

    override suspend fun load(key: SettingKey): SettingValue? {
        val raw = store.load(key.key).firstOrNull() ?: return null
        return SettingValue(key, raw)
    }

    override suspend fun save(value: SettingValue) {
        store.save(value.key.key, value.value)
    }
}
