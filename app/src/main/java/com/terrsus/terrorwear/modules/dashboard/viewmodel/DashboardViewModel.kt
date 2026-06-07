package com.terrsus.terrorwear.modules.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun updateGreeting(name: String) {
        _uiState.value = _uiState.value.copy(greetingName = name)
    }
}

data class DashboardUiState(
    val greetingName: String = "Android"
)