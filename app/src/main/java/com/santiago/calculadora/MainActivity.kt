package com.santiago.calculadora

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.math.BigDecimal
import java.math.MathContext

class MainActivity : AppCompatActivity() {

    // ---------- Vistas (se enlazan con findViewById en onCreate) ----------
    private lateinit var etPantalla: EditText
    private lateinit var tvExpresion: TextView
    private lateinit var tvHistorial: TextView

    // ---------- Estado de la calculadora ----------
    // Primer número de la operación (null si todavía no hay ninguno)
    private var primerOperando: Double? = null

    // Operador elegido: "+", "-", "*" o "/" (null si todavía no hay ninguno)
    private var operadorActual: String? = null

    // true cuando el siguiente dígito debe reemplazar lo que hay en pantalla
    // (por ejemplo, justo después de elegir un operador o de calcular un resultado)
    private var escribiendoNuevoNumero = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Respeta las barras del sistema y conserva el margen de 16dp del diseño
        val margen = (16 * resources.displayMetrics.density).toInt()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + margen,
                systemBars.top + margen,
                systemBars.right + margen,
                systemBars.bottom + margen
            )
            insets
        }

        // Enlazamos las vistas del XML con variables de Kotlin
        etPantalla = findViewById(R.id.etPantalla)
        tvExpresion = findViewById(R.id.tvExpresion)
        tvHistorial = findViewById(R.id.tvHistorial)

        // El EditText sirve como pantalla: no queremos que salga el teclado del sistema
        etPantalla.showSoftInputOnFocus = false

        configurarBotones()
    }

    /**
     * Asigna un OnClickListener a cada botón.
     * Un solo listener (digitoClick) atiende a los 10 dígitos: lee el texto del botón presionado.
     */
    private fun configurarBotones() {
        // Un listener compartido para todos los dígitos
        val digitoClick = View.OnClickListener { vista ->
            val boton = vista as Button
            agregarDigito(boton.text.toString())
        }
        val idsDigitos = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        for (id in idsDigitos) {
            findViewById<Button>(id).setOnClickListener(digitoClick)
        }

        findViewById<Button>(R.id.btnPunto).setOnClickListener { agregarPunto() }
        findViewById<Button>(R.id.btnLimpiar).setOnClickListener { limpiarTodo() }
        findViewById<Button>(R.id.btnBorrar).setOnClickListener { borrarUltimo() }
        findViewById<Button>(R.id.btnSigno).setOnClickListener { cambiarSigno() }
        findViewById<Button>(R.id.btnSumar).setOnClickListener { presionarOperador("+") }
        findViewById<Button>(R.id.btnIgual).setOnClickListener { presionarIgual() }
    }

    // =====================================================================
    //  ESCRITURA DEL NÚMERO EN PANTALLA
    // =====================================================================

    /**
     * Añade un dígito al número de la pantalla.
     * Si estamos empezando un número nuevo, reemplaza el texto; si la pantalla
     * solo tiene "0", también lo reemplaza para evitar ceros a la izquierda ("007").
     */
    private fun agregarDigito(digito: String) {
        val actual = etPantalla.text.toString()
        val nuevo = when {
            escribiendoNuevoNumero -> digito
            actual == "0" -> digito
            actual == "-0" -> "-$digito"
            else -> actual + digito
        }
        escribiendoNuevoNumero = false
        etPantalla.setText(nuevo)
    }

    /**
     * Añade el punto decimal. Solo se permite uno por número.
     * Si empezamos un número nuevo, queda "0." para que se vea bien.
     */
    private fun agregarPunto() {
        if (escribiendoNuevoNumero) {
            etPantalla.setText("0.")
            escribiendoNuevoNumero = false
            return
        }
        val actual = etPantalla.text.toString()
        if (!actual.contains(".")) {
            etPantalla.setText("$actual.")
        }
    }

    /**
     * Botón C: deja la calculadora como recién abierta (pantalla, expresión y estado).
     */
    private fun limpiarTodo() {
        primerOperando = null
        operadorActual = null
        escribiendoNuevoNumero = false
        etPantalla.setText(getString(R.string.cero))
        tvExpresion.text = ""
    }

    /**
     * Botón ⌫: borra el último carácter. Si no queda nada (o solo un "-"), vuelve a "0".
     */
    private fun borrarUltimo() {
        val actual = etPantalla.text.toString()
        val nuevo = actual.dropLast(1)
        if (nuevo.isEmpty() || nuevo == "-") {
            etPantalla.setText(getString(R.string.cero))
        } else {
            etPantalla.setText(nuevo)
        }
    }

    /**
     * Botón ±: cambia el signo del número en pantalla (el "0" se queda igual).
     */
    private fun cambiarSigno() {
        val actual = etPantalla.text.toString()
        if (actual == "0") return
        if (actual.startsWith("-")) {
            etPantalla.setText(actual.removePrefix("-"))
        } else {
            etPantalla.setText("-$actual")
        }
    }

    // =====================================================================
    //  OPERADORES Y RESULTADO
    // =====================================================================

    /**
     * Se llama al presionar un botón de operador (+, -, *, /).
     *  - Si ya había una operación pendiente y el usuario escribió el segundo número,
     *    primero calcula lo anterior (así se pueden encadenar: 12 + 5 + 3).
     *  - Si el usuario cambia de operador sin escribir el segundo número,
     *    solo reemplaza el operador.
     */
    private fun presionarOperador(operador: String) {
        val numeroEnPantalla = leerPantalla() ?: return
        val anterior = primerOperando

        if (anterior != null && operadorActual != null) {
            if (!escribiendoNuevoNumero) {
                // Había "12 +" y se escribió "5": calculamos 12 + 5 y seguimos con el resultado
                val resultado = calcular(anterior, numeroEnPantalla) ?: return
                primerOperando = resultado
                etPantalla.setText(formatear(resultado))
            }
            // Si escribiendoNuevoNumero es true, solo cambia el operador (se hace abajo)
        } else {
            primerOperando = numeroEnPantalla
        }

        operadorActual = operador
        escribiendoNuevoNumero = true
        tvExpresion.text = "${formatear(primerOperando!!)} ${simbolo(operador)}"
    }

    /**
     * Botón =: ejecuta la operación pendiente y muestra el resultado.
     * No hace nada si falta el operador o el segundo número.
     */
    private fun presionarIgual() {
        val a = primerOperando ?: return
        val operador = operadorActual ?: return
        if (escribiendoNuevoNumero) return // todavía no se escribió el segundo número
        val b = leerPantalla() ?: return

        val resultado = calcular(a, b) ?: return
        tvExpresion.text = "${formatear(a)} ${simbolo(operador)} ${formatear(b)} ="
        etPantalla.setText(formatear(resultado))

        // La operación terminó: el siguiente dígito empieza un número nuevo
        primerOperando = null
        operadorActual = null
        escribiendoNuevoNumero = true
    }

    /**
     * Elige qué función usar según el operador guardado.
     * Devuelve null si no se pudo calcular.
     */
    private fun calcular(a: Double, b: Double): Double? {
        return when (operadorActual) {
            "+" -> sumar(a, b)
            else -> null
        }
    }

    /** Convierte el operador interno ("*", "/", "-") al símbolo que se muestra al usuario. */
    private fun simbolo(operador: String): String {
        return when (operador) {
            "*" -> "×"
            "/" -> "÷"
            "-" -> "−"
            else -> operador
        }
    }

    // =====================================================================
    //  LAS CUATRO OPERACIONES
    // =====================================================================

    /** Suma: devuelve a + b. */
    private fun sumar(a: Double, b: Double): Double {
        return a + b
    }

    // =====================================================================
    //  UTILIDADES
    // =====================================================================

    /**
     * Lee el número que hay en pantalla como Double.
     * Devuelve null si el texto no es un número válido (por ejemplo "-" o vacío).
     */
    private fun leerPantalla(): Double? {
        return etPantalla.text.toString().toDoubleOrNull()
    }

    /**
     * Convierte un Double en texto amigable:
     *  - 17.0 se muestra como "17" (sin ".0")
     *  - 0.1 + 0.2 se muestra como "0.3" (máximo 10 cifras significativas)
     *  - nunca usa notación científica para números normales
     */
    private fun formatear(valor: Double): String {
        val redondeado = BigDecimal(valor).round(MathContext(10)).stripTrailingZeros()
        val texto = redondeado.toPlainString()
        return if (texto == "-0") "0" else texto
    }
}
