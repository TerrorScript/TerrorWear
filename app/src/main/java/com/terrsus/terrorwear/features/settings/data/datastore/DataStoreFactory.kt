package com.terrsus.terrorwear.features.settings.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * Factory for creating DataStore instances used by the settings subsystem.
 *
 * This abstraction keeps DataStore construction out of the repository layer
 * and allows for easier testing and future migration to encrypted storage.
 */
object DataStoreFactory {

    private const val SETTINGS_FILE = "settings.preferences_pb"

    /**
     * Creates a Preference DataStore instance for application settings.
     *
     * @param context Application context used to resolve the storage file.
     */
    fun createSettingsStore(context: Context) =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(SETTINGS_FILE) }
        )
}