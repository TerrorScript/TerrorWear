package com.terrsus.terrorwear.features.settings.domain.model

/**
 * Represents a stored setting value.
 *
 * Settings are stored as strings at the persistence layer, but exposed
 * as typed values at the domain layer.
 */
data class SettingValue(
    val key: SettingKey,
    val value: String
)
