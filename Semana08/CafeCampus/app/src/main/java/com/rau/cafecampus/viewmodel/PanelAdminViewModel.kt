package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import com.rau.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class PanelMetrics(
    val ventasTotales: Double = 0.0,
    val totalPedidos: Int = 0,
    val pendientesCount: Int = 0,
    val enPreparacionCount: Int = 0,
    val listosCount: Int = 0,
    val entregadosCount: Int = 0,
    val canceladosCount: Int = 0
)

class PanelAdminViewModel(
    repository: CafeRepository
) : ViewModel() {

    val todosLosPedidos: StateFlow<List<PedidoConDetalle>> = repository.obtenerTodosLosPedidos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val metricas: StateFlow<PanelMetrics> = todosLosPedidos.map { pedidos ->
        val validos = pedidos.map { it.pedido }.filter { it.estado.uppercase() != "CANCELADO" }
        val ventas = validos.sumOf { it.total }

        PanelMetrics(
            ventasTotales = ventas,
            totalPedidos = validos.size,
            pendientesCount = pedidos.count { it.pedido.estado.uppercase() == "PENDIENTE" },
            enPreparacionCount = pedidos.count { it.pedido.estado.uppercase() == "EN_PREPARACION" },
            listosCount = pedidos.count { it.pedido.estado.uppercase() == "LISTO" },
            entregadosCount = pedidos.count { it.pedido.estado.uppercase() == "ENTREGADO" },
            canceladosCount = pedidos.count { it.pedido.estado.uppercase() == "CANCELADO" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PanelMetrics())
}
