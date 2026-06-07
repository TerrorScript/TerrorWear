package com.terrsus.terrorwear.features.storage.domain.usecase

import com.terrsus.terrorwear.features.storage.domain.model.StoredHighscore
import com.terrsus.terrorwear.features.storage.domain.repository.HighscoreRepository

/**
 * Provides high-level operations for loading and saving high-score data.
 *
 * This interactor groups all high-score related use cases into a single,
 * cohesive API. It abstracts away the underlying persistence mechanism
 * (Room, file storage, encrypted storage, etc.) and exposes a clean domain
 * interface for the rest of the application.
 */
class HighscoreInteractor(
    private val repository: HighscoreRepository
) {

    /**
     * Loads the stored high-score entry, or null if none exists.
     */
    suspend fun load(): StoredHighscore? =
        repository.load()

    /**
     * Persists or updates the stored high-score entry.
     */
    suspend fun save(score: StoredHighscore) {
        repository.save(score)
    }
}