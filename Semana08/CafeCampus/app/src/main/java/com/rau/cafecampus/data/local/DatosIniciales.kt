package com.rau.cafecampus.data.local

import com.rau.cafecampus.data.local.entity.Producto
import com.rau.cafecampus.data.local.entity.Usuario

object DatosIniciales {
    val usuariosIniciales = listOf(
        Usuario(
            id = 1,
            nombre = "Administrador",
            correo = "admin@cafecampus.com",
            clave = "admin123",
            rol = "ADMIN"
        ),
        Usuario(
            id = 2,
            nombre = "Juan Pérez",
            correo = "estudiante@cafecampus.com",
            clave = "123456",
            rol = "CLIENTE"
        )
    )

    val productosIniciales = listOf(
        Producto(
            id = 1,
            nombre = "Café Americano",
            descripcion = "Café pasado con agua caliente",
            precio = 5.0,
            categoria = "BEBIDAS",
            disponible = true
        ),
        Producto(
            id = 2,
            nombre = "Café Latte",
            descripcion = "Espresso con leche al vapor",
            precio = 7.5,
            categoria = "BEBIDAS",
            disponible = true
        ),
        Producto(
            id = 3,
            nombre = "Sándwich de Pollo",
            descripcion = "Pan integral con pollo deshilachado y mayonesa",
            precio = 9.0,
            categoria = "COMIDAS",
            disponible = true
        ),
        Producto(
            id = 4,
            nombre = "Muffin de Chispas de Chocolate",
            descripcion = "Bizcocho esponjoso con chispas de chocolate",
            precio = 4.5,
            categoria = "POSTRES",
            disponible = true
        )
    )
}