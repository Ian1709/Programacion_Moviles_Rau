package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import com.rau.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

data class PanelMetrics(
    val ventasTotales: Double = 0.0,
    val totalPedidos: Int = 0,
    val pendientesCount: Int = 0,
    val enPreparacionCount: Int = 0,
    val listosCount: Int = 0,
    val entregadosCount: Int = 0,
    val canceladosCount: Int = 0
)

data class HoraEstadistica(
    val horaTexto: String,
    val horaInt: Int,
    val cantidadPedidos: Int
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

    // Gráfico real de pedidos por hora del día desde Room (excluyendo cancelados)
    val pedidosPorHora: StateFlow<List<HoraEstadistica>> = repository.obtenerPedidosValidos().map { pedidos ->
        val horasDeseadas = listOf(8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
        val cal = Calendar.getInstance()

        val agruparPorHora = pedidos.groupBy { pedido ->
            cal.timeInMillis = pedido.fecha
            cal.get(Calendar.HOUR_OF_DAY)
        }

        horasDeseadas.map { h ->
            HoraEstadistica(
                horaTexto = "${h}h",
                horaInt = h,
                cantidadPedidos = agruparPorHora[h]?.size ?: 0
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
