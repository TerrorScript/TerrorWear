package com.terrsus.terrorwear.features.storage.serialization

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor

/**
 * Shared CBOR serializer instance configured for compact binary encoding.
 *
 * CBOR is used for efficient on‑device persistence of structured data
 * such as trusted devices, pairing keys, and future telemetry snapshots.
 */
@OptIn(ExperimentalSerializationApi::class)
object CborSerializer {
    val cbor = Cbor { ignoreUnknownKeys = true }
}