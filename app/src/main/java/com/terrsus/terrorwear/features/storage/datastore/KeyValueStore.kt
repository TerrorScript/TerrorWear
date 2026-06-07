package com.terrsus.terrorwear.features.storage.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Simple key-value wrapper around DataStore<Preferences>.
 */
class KeyValueStore(
    private val dataStore: androidx.datastore.core.DataStore<Preferences>
) {

    fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data.map { prefs -> prefs[key] ?: default }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { prefs -> prefs[key] = value }
    }
}
