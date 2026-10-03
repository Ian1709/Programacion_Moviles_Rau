# Documentación de Prompts - Mejora IA TECSUP Store

## Objetivo
Documentar los prompts y requerimientos utilizados en el desarrollo de la aplicación Jetpack Compose (TECSUPstore) en la rama `mejora-ia-tecsupstore`, enfocados en la incorporación de un sistema de contador de favoritos dinámico integrado en las tarjetas de producto, la barra superior, y la barra de navegación lateral (Drawer).

---

## 1. Prompt 1: Callback en TarjetaProducto y Estado en AppNavegacion

### Prompt Utilizado
> Tengo una app en Jetpack Compose. Necesito modificar mi componente TarjetaProducto para agregarle un evento callback lambda onAgregarFavorito: () -> Unit que se active cuando el usuario presione la opción 'Favoritos' dentro de su DropdownMenu. También necesito actualizar AppNavegacion para declarar un estado var contadorFavoritos by remember { mutableStateOf(0) } y pasarlo como callback a cada tarjeta. Muestra solo el código actualizado de TarjetaProducto.kt y AppNavegacion.kt. Todo trabajalo en la rama mejora-ia-tecsupstore

### Resumen de la Solución
- **TarjetaProducto.kt**: Se añadió el parámetro `onAgregarFavorito: () -> Unit` al componente y se invocó al hacer clic en la opción "Favoritos" del `DropdownMenu`.
- **AppNavegacion.kt**: Se declaró el estado `var contadorFavoritos by remember { mutableStateOf(0) }`, se mostró dicho contador en la barra superior (`TopAppBar`), y se pasó el callback `{ contadorFavoritos++ }` a cada `TarjetaProducto`.

---

## 2. Prompt 2: Insignia (Badge) Dinámica en AppDrawer

### Prompt Utilizado
> Ahora necesito actualizar AppDrawer.kt en Jetpack Compose. Modifica la función AppDrawer para que reciba un nuevo parámetro contadorFavoritos: Int. Dentro de la lista de ítems del ModalDrawerSheet, agrega una insignia utilizando el parámetro badge de NavigationDrawerItem con la sintaxis badge = { if (item.titulo == "Favoritos" && contadorFavoritos > 0) Badge { Text(contadorFavoritos.toString()) } }. Muestra el código completo actualizado para AppDrawer.kt y la llamada dentro de AppNavegacion.kt. Todo trabajalo en la rama mejora-ia-tecsupstore

### Resumen de la Solución
- **AppDrawner.kt (AppDrawer)**: Se modificó la función para aceptar `contadorFavoritos: Int`, se estructuraron las opciones del menú con una clase de datos `DrawerItem(val titulo: String)`, y se añadió el parámetro `badge` en el `NavigationDrawerItem` para mostrar condicionalmente un contador de favoritos cuando sea mayor a 0.
- **AppNavegacion.kt**: Se actualizó la invocación del componente de navegación para pasar el estado `contadorFavoritos`.
