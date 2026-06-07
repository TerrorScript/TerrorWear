package com.terrsus.terrorwear.features.storage.serialization

import kotlinx.serialization.json.Json

/**
 * Shared JSON serializer instance for human‑readable structured data.
 *
 * Primarily used for debugging, logs, or exporting structured state.
 */
object JsonSerializer {
    val json = Json { ignoreUnknownKeys = true }
}