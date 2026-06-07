package com.terrsus.terrorwear.features.storage.domain.usecase

import com.terrsus.terrorwear.features.settings.domain.model.SettingKey
import com.terrsus.terrorwear.features.settings.domain.model.SettingValue
import com.terrsus.terrorwear.features.settings.domain.repository.SettingsRepository

/**
 * Provides operations for loading and saving application settings.
 *
 * This interactor abstracts over the underlying settings storage mechanism
 * and exposes a clean domain API for retrieving and updating configuration
 * values.
 */
class SettingsInteractor(
    private val repository: SettingsRepository
) {

    /**
     * Loads the value for the given setting key.
     *
     * @param key Strongly-typed setting identifier.
     * @return The stored value as a raw string, or null if not present.
     */
    suspend fun load(key: SettingKey): String? =
        repository.load(key)?.value

    /**
     * Saves or updates the value for the given setting key.
     *
     * @param key Strongly-typed setting identifier.
     * @param value The raw string value to persist.
     */
    suspend fun save(key: SettingKey, value: String) {
        repository.save(
            SettingValue(
                key = key,
                value = value
            )
        )
    }
}