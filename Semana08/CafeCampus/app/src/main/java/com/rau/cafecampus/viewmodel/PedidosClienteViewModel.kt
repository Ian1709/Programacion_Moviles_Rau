package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.entity.DetallePedido
import com.rau.cafecampus.data.local.entity.Pedido
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import com.rau.cafecampus.data.prefs.PreferenciasStore
import com.rau.cafecampus.data.repository.CafeRepository
import com.rau.cafecampus.model.ItemCarrito
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ConfirmarPedidoUiState(
    val horaRecojo: String = "10:30",
    val pago: String = "Pagar en caja",
    val nota: String = "",
    val estaCargando: Boolean = false,
    val mensajeError: String? = null,
    val pedidoIdExito: Long? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosClienteViewModel(
    private val repository: CafeRepository,
    preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmarPedidoUiState())
    val uiState: StateFlow<ConfirmarPedidoUiState> = _uiState.asStateFlow()

    // SharedFlow con replay = 1 para retener la notificación al cambiar de administrador a estudiante
    private val _eventosNotificacion = MutableSharedFlow<String>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val eventosNotificacion: SharedFlow<String> = _eventosNotificacion.asSharedFlow()

    val userId: StateFlow<Int> = preferenciasStore.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2) // Fallback al estudiante por defecto (ID = 2)

    // Flujo de pedidos del cliente en tiempo real
    val misPedidos: StateFlow<List<PedidoConDetalle>> = userId
        .flatMapLatest { id ->
            val idEfectivo = if (id > 0) id else 2
            repository.obtenerPedidosPorCliente(idEfectivo)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private var estadosPrevios = mapOf<Long, String>()

    init {
        // Escuchar cambios de estado en Room en tiempo real
        viewModelScope.launch {
            misPedidos.collect { lista ->
                val mapaNuevo = lista.associate { it.pedido.id to it.pedido.estado }
                if (estadosPrevios.isNotEmpty()) {
                    mapaNuevo.forEach { (id, nuevoEstado) ->
                        val estadoAnterior = estadosPrevios[id]
                        if (estadoAnterior != null && estadoAnterior != nuevoEstado) {
                            val mensajeValidacion = if (nuevoEstado == "EN_PREPARACION" || nuevoEstado == "VALIDADO") {
                                "🔔 ¡Tu pedido #%04d ha sido validado con éxito por el administrador!".format(id)
                            } else {
                                "🔔 ¡Tu pedido #%04d ha sido marcado como '%s' por el administrador!".format(id, nuevoEstado)
                            }
                            _eventosNotificacion.emit(mensajeValidacion)
                        }
                    }
                }
                estadosPrevios = mapaNuevo
            }
        }
    }

    fun onHoraRecojoChange(nuevaHora: String) {
        _uiState.value = _uiState.value.copy(horaRecojo = nuevaHora)
    }

    fun onPagoChange(nuevoPago: String) {
        _uiState.value = _uiState.value.copy(pago = nuevoPago)
    }

    fun onNotaChange(nuevaNota: String) {
        _uiState.value = _uiState.value.copy(nota = nuevaNota)
    }

    fun validarPedidoPorAdmin(pedidoId: Long, nuevoEstado: String = "VALIDADO") {
        viewModelScope.launch {
            repository.actualizarEstadoPedido(pedidoId, nuevoEstado)
            _eventosNotificacion.emit("🔔 ¡Tu pedido #%04d ha sido validado con éxito por el administrador!".format(pedidoId))
        }
    }

    fun confirmarPedido(
        itemsCarrito: List<ItemCarrito>,
        onExito: (Long) -> Unit
    ) {
        if (itemsCarrito.isEmpty()) {
            _uiState.value = _uiState.value.copy(mensajeError = "El carrito está vacío")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(estaCargando = true, mensajeError = null)
            val clienteId = if (userId.value > 0) userId.value else 2

            val totalCalculado = itemsCarrito.sumOf { it.subtotal }

            val nuevoPedido = Pedido(
                clienteId = clienteId,
                estado = "PENDIENTE",
                horaRecojo = _uiState.value.horaRecojo,
                pago = _uiState.value.pago,
                nota = _uiState.value.nota,
                total = totalCalculado,
                fecha = System.currentTimeMillis()
            )

            val detalles = itemsCarrito.map { item ->
                DetallePedido(
                    pedidoId = 0,
                    productoId = item.producto.id,
                    nombreProducto = item.producto.nombre,
                    cantidad = item.cantidad,
                    precioUnitario = item.producto.precio
                )
            }

            try {
                val pedidoId = repository.crearPedido(nuevoPedido, detalles)
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    pedidoIdExito = pedidoId
                )
                _eventosNotificacion.emit("🎉 ¡Pedido #%04d registrado con éxito!".format(pedidoId))
                onExito(pedidoId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    estaCargando = false,
                    mensajeError = "Error al registrar el pedido: ${e.message}"
                )
            }
        }
    }

    fun repetirPedido(
        pedidoConDetalle: PedidoConDetalle,
        menuViewModel: MenuViewModel,
        onResultado: (String) -> Unit
    ) {
        viewModelScope.launch {
            val agregados = mutableListOf<String>()
            val omitidos = mutableListOf<String>()

            for (detalle in pedidoConDetalle.detalles) {
                val productoActual = repository.obtenerProductoPorId(detalle.productoId)
                if (productoActual != null && productoActual.disponible) {
                    menuViewModel.agregarAlCarrito(productoActual, detalle.cantidad)
                    agregados.add(productoActual.nombre)
                } else {
                    omitidos.add(detalle.nombreProducto)
                }
            }

            val mensaje = when {
                agregados.isNotEmpty() && omitidos.isNotEmpty() ->
                    "Se cargaron los productos al carrito. Omitidos por no estar disponibles: ${omitidos.joinToString(", ")}"
                agregados.isNotEmpty() ->
                    "¡Pedido #${pedidoConDetalle.pedido.id} cargado nuevamente en el carrito!"
                else ->
                    "No se pudo repetir el pedido: los productos ya no están disponibles."
            }

            onResultado(mensaje)
        }
    }

    fun resetEstado() {
        _uiState.value = ConfirmarPedidoUiState()
    }
}
