package com.terrsus.terrorwear.features.settings.domain.usecase

import com.terrsus.terrorwear.features.settings.domain.model.SettingKey
import com.terrsus.terrorwear.features.settings.domain.model.SettingValue
import com.terrsus.terrorwear.features.settings.domain.repository.SettingsRepository

/**
 * High-level operations for loading and saving application settings.
 *
 * This interactor groups all settings-related use cases into a single,
 * cohesive API. It abstracts away the underlying persistence mechanism
 * and exposes a clean domain interface for the rest of the application.
 */
class SettingsInteractor(
    private val repository: SettingsRepository
) {

    /**
     * Loads the value for the given setting key.
     */
    suspend fun load(key: SettingKey): SettingValue? =
        repository.load(key)

    /**
     * Saves or updates the value for the given setting key.
     */
    suspend fun save(key: SettingKey, value: String) {
        repository.save(SettingValue(key, value))
    }
}
