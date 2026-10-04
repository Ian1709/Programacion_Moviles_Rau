# PROMPTS DE LA FASE 2: Mejora Obligatoria con IA

Este documento registra los prompts utilizados para guiar a la Inteligencia Artificial (Gemini) durante la implementación de la FASE 2 en el proyecto MiBodega.

El objetivo principal de esta fase fue implementar la MEJORA OBLIGATORIA: Un buscador en tiempo real que funcionara en combinación simultánea con el filtro de categorías (LazyRow), desarrollando todo en una rama separada (`mejora-ia-bodega`) y dividiendo el proceso en 4 commits lógicos.

---

### PROMPT 1: Solicitud inicial y definición de la hoja de ruta
**Objetivo:** Establecer el rol del asistente, el contexto del proyecto (rama `mejora-ia-bodega`), los requisitos técnicos (filtros combinados) y la estructura estricta de 4 commits.

**Texto del prompt enviado a la IA:**
> Actúa como un Desarrollador Senior de Android Jetpack Compose y experto en Git.
> CONTEXTO DEL PROYECTO: Hemos completado con éxito la Fase 1 en la rama main dentro de Semana06/MiBodega. Ahora iniciaremos la FASE 2 (rama mejora-ia-bodega).
> OBJETIVO PRINCIPAL DE LA FASE 2: Implementar la MEJORA OBLIGATORIA de la Tarea y generar la documentación requerida mediante exactamente 4 COMMITS DESCRIPTIVOS en la rama mejora-ia-bodega.
> REQUISITOS TÉCNICOS:
> 1. Buscador en tiempo real (PantallaInicio.kt):
> - El campo de búsqueda OutlinedTextField de PantallaInicio debe filtrar la lista de productos dinámicamente a medida que el usuario escribe.
> - COMBINACIÓN DE FILTROS: La lista debe responder simultáneamente a DOS filtros activos: el texto ingresado en la barra de búsqueda Y la categoría seleccionada en el LazyRow. Ambos filtros deben operar juntos, jamás reemplazarse.
> 2. Documentación PROMPTS.md:
> - Crear un archivo PROMPTS.md en la raíz de Semana06/MiBodega registrando los prompts utilizados durante esta segunda fase y explicaciones breves del proceso.
> REGLAS DE GIT Y FORMATO DE ENTREGAS:
> - Todos los comandos deben ejecutarse desde la raíz del repositorio (C:\Users\Ian\Programacion_Moviles_Rau).
> - La Fase 2 debe desarrollarse en la rama mejora-ia-bodega.
> HOJA DE RUTA DE LOS 4 COMMITS EN FASE 2:
> - Commit 1: Lógica del buscador en tiempo real y estado mutable en PantallaInicio.kt.
> - Commit 2: Lógica combinada de filtros (Buscador + LazyRow de categorías) y actualización del grid/listado.
> - Commit 3: Refinamiento visual y UI de la barra de búsqueda en tiempo real.
> - Commit 4: Creación y registro completo del archivo PROMPTS.md.
> POR FAVOR, EMPIEZA ENTREGANDO EL CÓDIGO Y COMANDOS DE GIT PARA EL COMMIT 1 DE LA FASE 2. Espera mi confirmación para avanzar al Commit 2.

**Explicación del proceso:** La IA identificó la pantalla `PantallaInicio.kt` y expuso el estado `textoBusqueda` en el `OutlinedTextField`. Añadió el `trailingIcon` de la "X" para borrar texto. Retornó los comandos de Git para crear la rama `mejora-ia-bodega` y registrar el Commit 1.

---

### PROMPT 2: Avance al Commit 2 (Filtros combinados)
**Objetivo:** Solicitar la implementación lógica que combina el estado de la búsqueda con el estado de la categoría seleccionada (Operación AND lógico).

**Texto del prompt enviado a la IA:**
> siguiente

**Explicación del proceso:** La IA modificó la colección de datos usando `.filter { }`. Creó tres variables booleanas (`coincideCategoria`, `coincideBusqueda`, `coincideFavoritos`) y las evaluó de forma simultánea con el operador `&&`. También ajustó los mensajes de texto del listado para reflejar que la búsqueda está activa. Retornó los comandos para el Commit 2.

---

### PROMPT 3: Avance al Commit 3 (Refinamiento Visual y Empty States)
**Objetivo:** Solicitar la mejora estética del componente de búsqueda y el rediseño de las vistas cuando la combinación de filtros no devuelve ningún producto.

**Texto del prompt enviado a la IA:**
> siguiente

**Explicación del proceso:** La IA actualizó el `OutlinedTextField` con esquinas redondeadas (`RoundedCornerShape(16.dp)`), colores con opacidad y un placeholder dinámico (ej. _"Buscar en abarrotes..."_). Diseñó un *Empty State* que muestra un ícono grande (`SearchOff`) y un texto explicativo claro si el buscador no arroja coincidencias dentro de una categoría específica. Resolvió algunos errores de importación y dependencias. Retornó los comandos para el Commit 3.

---

### PROMPT 4: Avance al Commit 4 (Documentación Final)
**Objetivo:** Solicitar la redacción y almacenamiento de este documento (`PROMPTS.md`) resumiendo la interacción.

**Texto del prompt enviado a la IA:**
> siguiente

**Explicación del proceso:** La IA procedió a generar este archivo Markdown documentando el flujo de trabajo y retorna los comandos finales para registrar el último commit de la Fase 2 y completar la actividad.
