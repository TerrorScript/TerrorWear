package com.terrsus.terrorwear.features.storage.datastore

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * Provides a Preferences DataStore instance.
 */
object DataStoreFactory {

    fun create(context: Context) =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("settings")
        }
}
