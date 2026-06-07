package com.terrsus.terrorwear.features.storage.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

/**
 * High-level settings API backed by KeyValueStore.
 */
class SettingsStore(
    private val kv: KeyValueStore
) {
    private val DARK_MODE = booleanPreferencesKey("dark_mode")

    fun darkMode(): Flow<Boolean> = kv.get(DARK_MODE, false)

    suspend fun setDarkMode(enabled: Boolean) = kv.set(DARK_MODE, enabled)
}
