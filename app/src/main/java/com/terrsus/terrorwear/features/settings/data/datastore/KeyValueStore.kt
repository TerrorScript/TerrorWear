package com.terrsus.terrorwear.features.settings.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Thin wrapper around a Preference DataStore that exposes simple
 * string-based key-value operations.
 *
 * Higher-level abstractions (such as [SettingsStore]) build on top of this
 * to provide typed access to application settings.
 */
class KeyValueStore(
    private val dataStore: DataStore<Preferences>
) {

    /**
     * Loads a string value for the given key, or null if not present.
     */
    fun load(key: String): Flow<String?> =
        dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(key)]
        }

    /**
     * Saves or updates a string value for the given key.
     */
    suspend fun save(key: String, value: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = value
        }
    }
}