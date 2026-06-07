package com.terrsus.terrorwear.features.settings.data.datastore

import kotlinx.coroutines.flow.Flow

/**
 * High-level typed accessor for application settings.
 *
 * This class provides a stable API for reading and writing settings without
 * exposing DataStore or Preferences directly to the domain layer.
 */
class SettingsStore(
    private val keyValueStore: KeyValueStore
) {

    /**
     * Loads the raw string value for a setting key.
     *
     * @param key Unique string identifier for the setting.
     */
    fun load(key: String): Flow<String?> =
        keyValueStore.load(key)

    /**
     * Saves or updates the raw string value for a setting key.
     *
     * @param key Unique string identifier for the setting.
     * @param value Value to persist.
     */
    suspend fun save(key: String, value: String) {
        keyValueStore.save(key, value)
    }
}