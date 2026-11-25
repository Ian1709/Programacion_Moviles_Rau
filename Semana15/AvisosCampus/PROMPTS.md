# Prompts de la Fase 2

Los prompts de esta tabla están redactados para Gemini integrado en Android Studio. Los resúmenes describen el criterio técnico aplicado; no son citas textuales de una conversación.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| «Agrega un canal de notificaciones `Urgentes` con `IMPORTANCE_HIGH` y conserva el canal `Avisos`. Haz que los recordatorios usen el canal urgente y mantén el permiso comprobado antes de mostrar notificaciones.» | La solución usa dos identificadores de canal, registra ambos al iniciar y selecciona el canal según el origen del aviso. | Se conservaron el canal actual para avisos comunes y el permiso existente; solo los avisos de origen `RECORDATORIO` usan el canal urgente. | Evita cambiar el comportamiento de los avisos existentes y permite que Android gestione la prioridad desde los ajustes del canal. |
| «Actualiza el constructor de notificaciones para que el texto completo pueda leerse al expandirla, usando `NotificationCompat.BigTextStyle`, sin quitar el título, el icono, el `PendingIntent` ni `setAutoCancel(true)`.» | La solución agrega `BigTextStyle` con el texto del aviso y conserva la configuración actual de la notificación. | Se añadió el estilo expandible al mismo contenido que ya entrega el modelo `Aviso`. | La notificación muestra el texto íntegro al expandirse sin duplicar datos ni cambiar las capas de MVVM. |
| «Agrega un `BroadcastReceiver` para `ACTION_POWER_CONNECTED` y `ACTION_POWER_DISCONNECTED`, un switch propio y registros en el historial. Mantén `Context` y el receptor fuera del ViewModel, registra el receptor mientras el switch esté activo y quítalo en `onDispose`.» | La solución separa el receptor del ViewModel, registra los eventos como avisos del Repository y administra el registro desde la pantalla con `DisposableEffect`. | Se creó `CargadorReceiver`, se añadió el origen `CARGADOR` y se usa `RECEIVER_NOT_EXPORTED`; el registro se limpia en `onDispose`. | Así el ViewModel sigue dependiendo solo del Repository, el switch controla la escucha y no queda un receptor registrado al salir de la composición. |

## Mejoras implementadas

1. Canal urgente para recordatorios.
2. Texto expandible con `BigTextStyle`.
3. Receptor de conexión del cargador con switch y registro en el historial.
