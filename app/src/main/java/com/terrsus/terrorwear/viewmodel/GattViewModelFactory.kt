package com.terrsus.terrorwear.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GattViewModelFactory(
    private val address: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GattViewModel(address) as T
    }
}
