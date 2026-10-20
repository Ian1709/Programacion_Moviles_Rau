# Prompts utilizados (FASE 2 - con IA) - CafeCampus

Durante la **Fase 2** del proyecto **CafeCampus**. Se implementaron tres mejoras funcionales (búsqueda de productos en el menú, repetir pedido desde el historial y gráfico de pedidos por hora) respetando la arquitectura **MVVM + Repository Pattern** con Kotlin, Jetpack Compose, Room y Coroutines.

## Prompt 1: Búsqueda de productos en el menú
  > "Implementa la funcionalidad de búsqueda de productos por nombre y descripción en la pantalla del menú de cliente de CafeCampus. Agrega un estado reactivo `_textoBusqueda` en `MenuViewModel` y combina mediante el operador `combine` los flujos `productosDisponibles`, `categoriaSeleccionada` y `textoBusqueda` para filtrar la lista en tiempo real. En `MenuScreen`, agrega un campo de búsqueda `CafeTextField` ubicado sobre la lista de productos y conéctalo al ViewModel. Garantiza la arquitectura MVVM sin realizar filtrados dentro de la vista Composable."

**Qué generó la IA**:

`MenuViewModel.kt`: Estado `_textoBusqueda` (`MutableStateFlow`), función `onTextoBusquedaChange()` y actualización del `StateFlow` `productosFiltrados` combinando los 3 flujos.
`MenuScreen.kt`: Campo de texto `CafeTextField` integrado en la parte superior del catálogo.

---

## Prompt 2: Repetir pedido desde el historial
  > "Agrega la funcionalidad de repetir un pedido anterior desde el historial de compras en CafeCampus. En `PedidoCard` añade un botón `🔄 Repetir`. Al pulsarlo, `PedidosClienteViewModel` debe consultar a Room mediante `CafeRepository.obtenerProductoPorId()` para verificar la disponibilidad actual de cada ítem. Si el producto está disponible, agrégalo al carrito en `MenuViewModel`; si no está disponible, omítelo e informa al estudiante mediante un mensaje en `SnackbarHost`. Toda la lógica debe abstracted en el ViewModel sin acceso directo a DAOs desde la UI."

**Qué generó la IA**:
  - `PedidosClienteViewModel.kt`: Función `repetirPedido(...)` que consulta Room a través del repositorio para verificar la disponibilidad actual de cada producto antes de cargarlo al carrito.
  - `PedidoCard.kt`: Botón visual `🔄 Repetir` en las tarjetas de pedidos.
  - `MisPedidosScreen.kt`: Notificaciones en `SnackbarHost` sobre los productos cargados u omitidos por no estar disponibles.

---

## Prompt 3: Gráfico real de pedidos por hora
* **Texto enviado a la IA**:
  > "Implementa un gráfico de pedidos por hora en el Panel de Control del Administrador (`PanelScreen`) alimentado por datos reales de Room. Añade en `PedidoDao` una consulta SQL `@Query` que obtenga los pedidos con estado distinto a 'CANCELADO' ordenados por fecha. Expón el método a través de la interfaz `CafeRepository` y su implementación. En `PanelAdminViewModel`, agrupa los pedidos por hora del día (8h a 17h) en un `StateFlow<List<HoraEstadistica>>`. En `PanelScreen`, dibuja las barras proporcionales resaltando la hora de mayor demanda."

**Qué generó la IA**:
  - `PedidoDao.kt`: Consulta SQL `@Query("SELECT * FROM pedidos WHERE estado != 'CANCELADO' ORDER BY fecha ASC") abstract fun obtenerPedidosValidos(): Flow<List<Pedido>>`.
  - `CafeRepository.kt` y `CafeRepositoryRoom.kt`: Método `obtenerPedidosValidos()`.
  - `PanelAdminViewModel.kt`: Data class `HoraEstadistica` y procesamiento del `StateFlow` `pedidosPorHora` agrupando pedidos por la hora del día.
  - `PanelScreen.kt`: Gráfico de barras dinámico que escala proporcionalmente con datos reales de Room.

---

## Reflexión
La IA aceleró significativamente el desarrollo al implementar flujos reactivos complejas con `combine` y consultas en tiempo real.

No pudo resolver por sí sola ciertas inconsistencias de compatibilidad de Gradle KSP y requeridos de permisos de AndroidManifest.

Cometió pequeños descuidos con el renderizado inicial de ampersands en el diagrama Mermaid que requirieron corrección.

Aprendí cómo `Flow` y `StateFlow` mantienen la base de datos Room sincronizada en tiempo real con Jetpack Compose.
Comprendí la importancia del patrón Repository para desacoplar completamente la interfaz de la persistencia de datos.
