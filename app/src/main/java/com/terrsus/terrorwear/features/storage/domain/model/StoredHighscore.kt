package com.terrsus.terrorwear.features.storage.domain.model

/**
 * Represents a recorded high‑score entry for a game or activity.
 *
 * This domain model is storage‑agnostic and may be persisted using Room,
 * encrypted files, or any other backend. Additional metadata (such as
 * game mode or duration) can be added as the feature evolves.
 *
 * @property id Unique identifier for the score entry.
 * @property score Numeric score value.
 * @property timestamp Epoch millis when the score was recorded.
 */
data class StoredHighscore(
    val id: Long = 0L,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
)
