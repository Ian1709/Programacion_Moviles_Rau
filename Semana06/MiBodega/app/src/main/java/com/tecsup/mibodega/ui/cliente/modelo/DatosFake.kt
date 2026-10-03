package com.tecsup.mibodega.ui.cliente.modelo

/**
 * Datos de ejemplo (fake) para mostrar la UI sin base de datos.
 */
val listaCategorias = listOf("Todos", "Abarrotes", "Bebidas", "Lácteos", "Snacks", "Limpieza")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz Costeño Extra 1kg",
        descripcion = "Arroz extra, grano largo selección superior 1 kg.",
        precio = 4.50,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor Premium 1L",
        descripcion = "Aceite vegetal 1 L alto en vitamina E y omega 3.",
        precio = 8.90,
        categoria = "Abarrotes"
    ),
    Producto(
        id = 3,
        nombre = "Leche Evaporada Gloria 400g",
        descripcion = "Leche evaporada entera lata de 400 g.",
        precio = 4.20,
        categoria = "Lácteos"
    ),
    Producto(
        id = 4,
        nombre = "Yogurt Gloria Fresa 1L",
        descripcion = "Yogurt bebible sabor fresa botella de 1 L.",
        precio = 6.80,
        categoria = "Lácteos"
    ),
    Producto(
        id = 5,
        nombre = "Galleta Oreo Paquete 126g",
        descripcion = "Galletas de chocolate rellenas paquete 126 g.",
        precio = 3.50,
        categoria = "Snacks"
    ),
    Producto(
        id = 6,
        nombre = "Papas Lay's Clásicas 160g",
        descripcion = "Papas fritas saladas crujientes bolsa 160 g.",
        precio = 5.90,
        categoria = "Snacks"
    ),
    Producto(
        id = 7,
        nombre = "Coca-Cola Original 1.5L",
        descripcion = "Bebida gaseosa sabor cola original botella 1.5 L.",
        precio = 6.50,
        categoria = "Bebidas"
    ),
    Producto(
        id = 8,
        nombre = "Inca Kola Sin Azúcar 1.5L",
        descripcion = "Gaseosa sabor nacional sin azúcar botella 1.5 L.",
        precio = 6.20,
        categoria = "Bebidas"
    ),
    Producto(
        id = 9,
        nombre = "Detergente Ariel 1kg",
        descripcion = "Detergente en polvo para ropa blanca y de color 1 kg.",
        precio = 11.50,
        categoria = "Limpieza"
    ),
    Producto(
        id = 10,
        nombre = "Jabón Líquido Aval 400ml",
        descripcion = "Jabón líquido para manos antibacterial 400 ml.",
        precio = 7.50,
        categoria = "Limpieza"
    )
)
