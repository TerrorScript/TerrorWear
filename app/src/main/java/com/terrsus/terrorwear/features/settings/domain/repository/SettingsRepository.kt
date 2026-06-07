package com.terrsus.terrorwear.features.settings.domain.repository

import com.terrsus.terrorwear.features.settings.domain.model.SettingKey
import com.terrsus.terrorwear.features.settings.domain.model.SettingValue

/**
 * Abstraction over application settings persistence.
 *
 * Implementations may use DataStore, Room, encrypted storage, or any
 * other backend. The domain layer remains storage-agnostic.
 */
interface SettingsRepository {

    /**
     * Loads the value associated with the given setting key.
     */
    suspend fun load(key: SettingKey): SettingValue?

    /**
     * Saves or updates the value for the given setting key.
     */
    suspend fun save(value: SettingValue)
}
