package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import com.rau.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PedidosAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _filtroEstado = MutableStateFlow("TODOS")
    val filtroEstado: StateFlow<String> = _filtroEstado.asStateFlow()

    val todosLosPedidos: StateFlow<List<PedidoConDetalle>> = repository.obtenerTodosLosPedidos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pedidosFiltrados: StateFlow<List<PedidoConDetalle>> = combine(
        todosLosPedidos,
        _filtroEstado
    ) { pedidos, filtro ->
        if (filtro == "TODOS") {
            pedidos
        } else {
            pedidos.filter { it.pedido.estado.equals(filtro, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionarFiltro(nuevoFiltro: String) {
        _filtroEstado.value = nuevoFiltro
    }

    fun actualizarEstadoPedido(pedidoId: Long, nuevoEstado: String) {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(pedidoId, nuevoEstado)
        }
    }
}
