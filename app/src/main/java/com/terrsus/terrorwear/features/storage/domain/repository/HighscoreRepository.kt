package com.terrsus.terrorwear.features.storage.domain.repository

import com.terrsus.terrorwear.features.storage.domain.model.StoredHighscore

/**
 * Abstraction over high-score persistence.
 */
interface HighscoreRepository {

    /**
     * Loads the single stored high-score entry, or null if none exists.
     */
    suspend fun load(): StoredHighscore?

    /**
     * Saves or replaces the stored high-score entry.
     */
    suspend fun save(score: StoredHighscore)
}