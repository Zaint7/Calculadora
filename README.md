# Calculadora

Aplicación de calculadora para Android, escrita en Kotlin con interfaz clásica de Views (layouts XML).
Realiza las cuatro operaciones básicas, guarda un historial y maneja los errores más comunes.

## Funcionalidades

- Cuatro operaciones: suma, resta, multiplicación y división, cada una en su propia función (`sumar`, `restar`, `multiplicar`, `dividir`).
- Operaciones encadenadas (`12 + 5 + 3 = 20`).
- Historial de operaciones con desplazamiento (`ScrollView`).
- Botones `C` (limpiar todo), `⌫` (borrar último carácter) y `±` (cambiar signo).
- Tecla Enter (teclado físico o emulador) para calcular.
- Manejo de errores con `Toast`: división entre cero y operador sin número.
- Resultados sin decimales sobrantes (`17`, no `17.0`; `0.1 + 0.2` se ve `0.3`).
- El botón del operador activo cambia de color.

## Tecnologías

- Kotlin
- Android Views con layouts XML (`LinearLayout`, `ScrollView`, `TableLayout`)
- `findViewById` y `OnClickListener`, sin librerías externas

## Temas del módulo aplicados

| Tema del curso | Dónde se evidencia |
|---|---|
| `TableLayout` (ejemplo de calculadora) | Teclado de botones |
| `LinearLayout` / `ScrollView` | Contenedor principal y historial |
| Botones (`Button`) y etiquetas (`TextView`) | Teclado, pantalla y títulos |
| Cuadro de texto (`EditText`) | Pantalla de entrada |
| Formato de texto (tamaño, color) en XML y desde Kotlin | Pantalla, historial, resultado |
| Añadir texto con `append()` | Historial de operaciones |
| `ScrollView` para extender la pantalla | Historial |
| `OnClickListener` | Todos los botones |
| `OnKeyListener` | Tecla Enter en el `EditText` calcula |
| `Toast` | Mensajes de error (división entre 0, entrada inválida) |
| Cambiar el color de un botón desde código | Botón del operador activo cambia de color |

## Cómo ejecutarla

1. Abrir la carpeta del proyecto en Android Studio.
2. Esperar a que termine la sincronización de Gradle.
3. Elegir un emulador o dispositivo y presionar **Run**.

## Capturas

![Captura de la calculadora](docs/captura1.png)

_(Pendiente: Santiago agregará la captura en `docs/captura1.png`.)_

## Autoría

Desarrollada de forma individual por Santiago Vargas (`Zaint7`) para la actividad del curso de desarrollo móvil Android.
