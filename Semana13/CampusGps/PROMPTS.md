# PROMPTS.md — Fase 2 de CampusGPS

El enunciado propone usar Gemini en Android Studio. Las propuestas y cambios registrados en esta entrega se trabajaron con Codex en el repositorio local; no atribuyo respuestas a Gemini que no ocurrieron. Revisé cada cambio contra la arquitectura del laboratorio y compilé la aplicación después de cada mejora.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega chips para filtrar por categoría Estudio, Comida, Eventos y Servicios en el mapa y la lista, conservando MVVM.» | Propuso mantener categoría seleccionada en el ViewModel y publicar los lugares filtrados mediante un flujo; los Composables renderizan los elementos visibles. | Implementé `categoriaSeleccionada`, `lugaresVisibles` y `filtrarPorCategoria` en `MapaViewModel`; agregué chips en `PantallaMapa`. El mapa y la lista reciben la misma lista filtrada. | La fuente de estado única evita que el mapa y la lista muestren resultados distintos. Limpiar la selección al cambiar a una categoría incompatible evita una cámara enfocada en un elemento oculto. |
| «Añade un selector de 100, 200 o 500 metros, dibuja el círculo alrededor de la ubicación actual y marca en la lista los lugares dentro del radio.» | Propuso exponer el radio desde el ViewModel, calcular la inclusión junto a la distancia y dibujar el círculo como una capa visual del mapa. | Añadí `radioMetros` y `dentroDelRadio`; el ViewModel recalcula los valores cuando cambia el radio. `MapaUbicacion` dibuja el `Circle` y `ListaLugares` muestra “Dentro del radio”. | La geometría del mapa permanece en la View y la regla de cercanía se mantiene fuera de los Composables. |
| «Aplica el estilo oscuro de Google Maps cuando el tema del sistema sea oscuro, sin mover esta decisión al ViewModel.» | Propuso cargar un `MapStyleOptions` desde un recurso raw y pasarlo a `MapProperties` cuando `isSystemInDarkTheme()` sea verdadero. | Añadí `res/raw/map_style_dark.json` y apliqué el estilo en `MapaUbicacion`; en tema claro se usa el estilo predeterminado. | Es una personalización de presentación, por lo que pertenece a la View. El recurso se puede revisar sin introducir contexto Android en el ViewModel. |

## Verificación realizada

- `assembleDebug` terminó correctamente después de cada una de las tres mejoras.
- La comprobación visual interactiva requiere ejecutar la app con clave de Maps válida y un emulador/dispositivo; la clave local disponible está vacía.
- La autoría de las decisiones técnicas y cualquier adaptación frente a propuestas corresponde al estudiante que revise y entregue el laboratorio.
