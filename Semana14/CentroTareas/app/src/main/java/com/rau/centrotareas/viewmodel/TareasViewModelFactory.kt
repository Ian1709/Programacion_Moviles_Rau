package com.rau.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rau.centrotareas.data.TareasRepositoryLocal

class TareasViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) return TareasViewModel(TareasRepositoryLocal()) as T
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
