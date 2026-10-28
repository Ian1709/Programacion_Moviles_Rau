package com.rau.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FormularioProductoUiState(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val categoria: String = "BEBIDAS",
    val imagenUrl: String = "",
    val disponible: Boolean = true,
    val estaCargando: Boolean = false,
    val mensajeError: String? = null,
    val guardadoExitoso: Boolean = false
)

class MenuAdminViewModel(
    private val repository: CafeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioProductoUiState())
    val uiState: StateFlow<FormularioProductoUiState> = _uiState.asStateFlow()

    private val _categoriaFiltro = MutableStateFlow("TODOS")
    val categoriaFiltro: StateFlow<String> = _categoriaFiltro.asStateFlow()

    val productosDisponibles: StateFlow<List<Producto>> = repository.obtenerProductosDisponibles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val productosFiltrados: StateFlow<List<Producto>> = combine(
        productosDisponibles,
        _categoriaFiltro
    ) { productos, cat ->
        if (cat == "TODOS") productos
        else productos.filter { it.categoria.equals(cat, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun seleccionarCategoriaFiltro(cat: String) {
        _categoriaFiltro.value = cat
    }

    fun cargarProductoParaEdicion(productoId: Int) {
        if (productoId <= 0) {
            _uiState.value = FormularioProductoUiState()
            return
        }

        viewModelScope.launch {
            val prod = repository.obtenerProductoPorId(productoId)
            if (prod != null) {
                _uiState.value = FormularioProductoUiState(
                    id = prod.id,
                    nombre = prod.nombre,
                    descripcion = prod.descripcion,
                    precio = prod.precio.toString(),
                    categoria = prod.categoria,
                    imagenUrl = prod.imagenUrl,
                    disponible = prod.disponible
                )
            }
        }
    }

    fun onNombreChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(nombre = nuevo, mensajeError = null)
    }

    fun onDescripcionChange(nueva: String) {
        _uiState.value = _uiState.value.copy(descripcion = nueva, mensajeError = null)
    }

    fun onPrecioChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(precio = nuevo, mensajeError = null)
    }

    fun onCategoriaChange(nueva: String) {
        _uiState.value = _uiState.value.copy(categoria = nueva)
    }

    fun onImagenUrlChange(nuevaUrl: String) {
        _uiState.value = _uiState.value.copy(imagenUrl = nuevaUrl)
    }

    fun onDisponibleChange(nuevo: Boolean) {
        _uiState.value = _uiState.value.copy(disponible = nuevo)
    }

    fun toggleDisponibilidad(producto: Producto) {
        viewModelScope.launch {
            val prodActualizado = producto.copy(disponible = !producto.disponible)
            repository.insertarProductos(listOf(prodActualizado))
        }
    }

    fun guardarProducto(onExito: () -> Unit) {
        val state = _uiState.value
        val nombre = state.nombre.trim()
        val descripcion = state.descripcion.trim()
        val precioVal = state.precio.toDoubleOrNull()

        if (nombre.isEmpty()) {
            _uiState.value = state.copy(mensajeError = "Ingrese el nombre del producto")
            return
        }
        if (descripcion.isEmpty()) {
            _uiState.value = state.copy(mensajeError = "Ingrese la descripción del producto")
            return
        }
        if (precioVal == null || precioVal <= 0.0) {
            _uiState.value = state.copy(mensajeError = "Ingrese un precio válido mayor a 0")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(estaCargando = true, mensajeError = null)
            val nuevoProd = Producto(
                id = state.id,
                nombre = nombre,
                descripcion = descripcion,
                precio = precioVal,
                categoria = state.categoria,
                imagenUrl = state.imagenUrl,
                disponible = state.disponible
            )
            try {
                repository.insertarProductos(listOf(nuevoProd))
                _uiState.value = FormularioProductoUiState(guardadoExitoso = true)
                onExito()
            } catch (e: Exception) {
                _uiState.value = state.copy(
                    estaCargando = false,
                    mensajeError = "Error al guardar: ${e.message}"
                )
            }
        }
    }

    fun resetFormulario() {
        _uiState.value = FormularioProductoUiState()
    }
}
