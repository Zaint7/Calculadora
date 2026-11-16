# Guion del video (4 a 6 minutos)

## 1. Presentación (≈ 30 s)
- Me llamo Santiago Vargas, usuario de GitHub `Zaint7`.
- Actividad: app "Calculadora" para Android con layouts XML, eventos y listeners.
- La actividad era grupal, pero la hice de forma individual porque no tengo grupo.

## 2. Demo de la app (≈ 1 min)
- Suma: `12 + 5 =` → `17`.
- Resta: `10 − 20 =` → `-10`.
- Multiplicación: `3 × 4 =` → `12`.
- División: `10 ÷ 4 =` → `2.5`.
- Error: `5 ÷ 0` → Toast "No se puede dividir entre cero".
- Encadenar: `12 + 5 + 3 =` → `20`.
- Mostrar `C`, `⌫`, `±` y la tecla Enter.
- Mostrar el historial y cómo se desplaza.
- Señalar el cambio de color del operador activo y el resultado en azul.

## 3. Recorrido por `activity_main.xml` (≈ 1 min 30 s)
- `LinearLayout` vertical como contenedor principal.
- `TextView` del título y `ScrollView` con el historial.
- `TextView` de la expresión y `EditText` de la pantalla (`inputType="none"`).
- `TableLayout` con `TableRow`, `layout_weight` y `layout_span` en el botón 0.
- Estilos de botones (`styles.xml`), colores (`colors.xml`) y textos (`strings.xml`).

## 4. Recorrido por `MainActivity.kt` (≈ 1 min 30 s)
- `findViewById` y `setOnClickListener` en `configurarBotones()`.
- Variables de estado: `primerOperando`, `operadorActual`, `escribiendoNuevoNumero`.
- Las cuatro funciones: `sumar`, `restar`, `multiplicar`, `dividir`, y el `when` de `calcular()`.
- `OnKeyListener` para la tecla Enter.
- `Toast` para los errores.
- `append()` en el historial y `fullScroll` en el `ScrollView`.
- Formato desde código: `setTextColor`, `setTextSize` y `setBackgroundResource`.

## 5. Recorrido por GitHub (≈ 30 s)
- Abrir `https://github.com/Zaint7/Calculadora/commits/main`.
- Mostrar los commits en orden: diseño, estructura base, suma, resta, multiplicación, división, historial/Enter/Toasts y README.
- Mostrar que el profesor está como colaborador del repositorio.

## 6. Cierre (≈ 20 s)
- Resumen de los temas aplicados: layouts, componentes, texto, eventos, listeners y Toast.
- Agradecer y despedirse.
