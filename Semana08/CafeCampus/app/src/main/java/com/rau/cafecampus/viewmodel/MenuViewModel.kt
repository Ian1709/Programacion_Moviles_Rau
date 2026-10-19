package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.repository.CafeRepository
import com.rau.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MenuUiState(
    val categoriaSeleccionada: String = "TODOS",
    val carrito: List<ItemCarrito> = emptyList(),
    val estaCargando: Boolean = false,
    val mensajeInfo: String? = null
)

class MenuViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    // Flujo de todos los productos disponibles en Room
    val productosDisponibles: StateFlow<List<Producto>> = repository.obtenerProductosDisponibles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Productos filtrados dinámicamente por categoría
    val productosFiltrados: StateFlow<List<Producto>> = combine(
        productosDisponibles,
        _uiState
    ) { productos, state ->
        if (state.categoriaSeleccionada == "TODOS") {
            productos
        } else {
            productos.filter { it.categoria.equals(state.categoriaSeleccionada, ignoreCase = true) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun seleccionarCategoria(categoria: String) {
        _uiState.value = _uiState.value.copy(categoriaSeleccionada = categoria)
    }

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        if (!producto.disponible || cantidad <= 0) return

        val carritoActual = _uiState.value.carrito.toMutableList()
        val index = carritoActual.indexOfFirst { it.producto.id == producto.id }

        if (index >= 0) {
            val itemExistente = carritoActual[index]
            carritoActual[index] = itemExistente.copy(cantidad = itemExistente.cantidad + cantidad)
        } else {
            carritoActual.add(ItemCarrito(producto = producto, cantidad = cantidad))
        }

        _uiState.value = _uiState.value.copy(
            carrito = carritoActual,
            mensajeInfo = "Se agregó ${producto.nombre} al carrito"
        )
    }

    fun actualizarCantidadItem(productoId: Int, nuevaCantidad: Int) {
        val carritoActual = _uiState.value.carrito.toMutableList()
        val index = carritoActual.indexOfFirst { it.producto.id == productoId }

        if (index >= 0) {
            if (nuevaCantidad <= 0) {
                carritoActual.removeAt(index)
            } else {
                carritoActual[index] = carritoActual[index].copy(cantidad = nuevaCantidad)
            }
            _uiState.value = _uiState.value.copy(carrito = carritoActual)
        }
    }

    fun removerDelCarrito(productoId: Int) {
        val carritoActual = _uiState.value.carrito.filter { it.producto.id != productoId }
        _uiState.value = _uiState.value.copy(carrito = carritoActual)
    }

    fun limpiarCarrito() {
        _uiState.value = _uiState.value.copy(carrito = emptyList())
    }

    fun obtenerProductoPorId(productoId: Int, onResultado: (Producto?) -> Unit) {
        viewModelScope.launch {
            val producto = repository.obtenerProductoPorId(productoId)
            onResultado(producto)
        }
    }

    fun totalCarrito(): Double = _uiState.value.carrito.sumOf { it.subtotal }

    fun totalItemsCarrito(): Int = _uiState.value.carrito.sumOf { it.cantidad }
}
