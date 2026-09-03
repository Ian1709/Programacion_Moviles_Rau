# Laboratorio 03: Registro de Producto con Jetpack Compose

**Estudiante:** Ian Alexander Rau Reyes  
**Curso:** Programación en Móviles - 4to Ciclo  
**Institución:** Tecsup


## Mejora con IA (Rama mejora-ia)

| Prompt que usé | Qué generó Gemini |                                                                                                                                                                                                                Qué acepté o corregí (y por qué) |
| :--- | :--- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| "Tomando en cuenta el proyecto anterior, ahora en la nueva rama creada agrega a PantallaRegistro validación para no permitir campos vacíos ni valores <= 0 mostrando un texto en rojo en lugar de la Card, y añade un botón Limpiar para vaciar el formulario." | Estructura con variables de estado adicionales (`mensajeError`), bloque `when` para validación de entradas numéricas y un botón `OutlinedButton` para limpiar. | Acepté la lógica de validación condicional. Corregí la proporción de ancho (`weight`) entre el botón Agregar y Limpiar (1.5f vs 0.9f) y cambié el estilo de texto y color de `OutlinedButton` para mantener la consistencia con el laboratorio. |