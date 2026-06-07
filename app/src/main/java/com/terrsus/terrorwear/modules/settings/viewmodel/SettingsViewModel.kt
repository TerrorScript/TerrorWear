package com.terrsus.terrorwear.modules.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrsus.terrorwear.features.settings.domain.model.SettingKey
import com.terrsus.terrorwear.features.settings.domain.usecase.SettingsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Settings module.
 *
 * Exposes reactive state for UI components and delegates persistence
 * operations to [SettingsInteractor].
 */
class SettingsViewModel(
    private val settings: SettingsInteractor
) : ViewModel() {

    private val _telemetryEnabled = MutableStateFlow(false)
    val telemetryEnabled: StateFlow<Boolean> = _telemetryEnabled

    fun loadSettings() {
        viewModelScope.launch {
            val value = settings.load(SettingKey.EnableTelemetry)
            _telemetryEnabled.value = value?.value == "true"
        }
    }

    fun setTelemetryEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settings.save(SettingKey.EnableTelemetry, enabled.toString())
            _telemetryEnabled.value = enabled
        }
    }
}