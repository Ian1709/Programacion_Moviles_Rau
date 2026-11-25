# Registro de prompts y decisiones

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Solicitud del laboratorio: agregar borrado de publicaciones con `@DELETE`, revisar `Response<Unit>` y llevarlo por API, Repository, ViewModel y pantalla. | No se consultó Gemini para esta implementación; Codex realizó el cambio en este hilo. | El Repository convierte la respuesta exitosa en `Result<Int>` con el código HTTP y lanza `HttpException(response)` ante una respuesta no exitosa. La UI conserva el detalle y muestra el código antes de volver a la lista. | Mantiene `HttpException` en data, preserva el contrato de `Response<Unit>` y hace visible el resultado simulado de JSONPlaceholder.
| Solicitud del laboratorio: agregar un campo que busque por título entre las publicaciones descargadas. | No se consultó Gemini para esta implementación; Codex realizó el cambio en este hilo. | La búsqueda se guarda en `StateFlow` del ViewModel y filtra localmente el resultado actual. No se envía una consulta de red al escribir. | El requisito es buscar entre los datos ya descargados y evitar tráfico innecesario; la búsqueda también respeta el filtro de usuario.
| Solicitud del laboratorio: crear un interceptor OkHttp que agregue `Accept: application/json` a cada petición. | No se consultó Gemini para esta implementación; Codex realizó el cambio en este hilo. | Se agregó `AcceptJsonInterceptor` antes del interceptor de logging en el `OkHttpClient`. | Así el encabezado aplica a todos los métodos y queda visible en el registro de OkHttp.

## Errores detectados y correcciones

- Android Gradle Plugin 9.4.1 ya aporta el plugin Kotlin integrado. Aplicar además `org.jetbrains.kotlin.android` producía un error por una extensión `kotlin` duplicada. Se retiró ese plugin externo y se mantuvieron los plugins Compose y Serialization con Kotlin 2.2.10.
- La primera compilación encontró un import incorrecto de `rememberSaveable` y una selección suspend con referencia de función. Se corrigieron a `androidx.compose.runtime.saveable.rememberSaveable` y una condición explícita entre `listar()` y `porUsuario()`.
- `CancellationException` se relanza tanto desde Repository como desde la carga paralela de detalle; no se transforma en un error de red visible.
- Las pruebas temporales confirmaron que `ignoreUnknownKeys` ignora campos nuevos y que falta de un campo obligatorio produce `SerializationException`. Se retiraron antes de los commits de entrega, como pide el laboratorio.

## Límite de JSONPlaceholder

Las respuestas de POST y DELETE son simuladas: el servidor no persiste el alta ni el borrado. La app refleja el resultado en la sesión local, sin afirmar que el estado remoto haya cambiado de forma permanente.
