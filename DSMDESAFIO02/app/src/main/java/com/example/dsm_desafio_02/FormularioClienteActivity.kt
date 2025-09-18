package com.example.dsm_desafio_02

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormularioClienteActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_cliente)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardarCliente)

        btnGuardar.setOnClickListener {
            validarYGuardarCliente()
        }
    }

    private fun validarYGuardarCliente() {
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim().lowercase()
        val telefono = etTelefono.text.toString().trim()

        // Validar que todos los campos estén llenos
        if (nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar nombre (al menos 2 caracteres)
        if (nombre.length < 2) {
            Toast.makeText(this, "El nombre debe tener al menos 2 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar correo electrónico
        if (!validarCorreo(correo)) {
            return
        }

        // Validar teléfono
        if (!validarTelefono(telefono)) {
            return
        }

        // Si todas las validaciones pasan, guardar cliente
        ClienteController.agregarCliente(nombre, correo, telefono) { exito ->
            if (exito) {
                Toast.makeText(this, "Cliente agregado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarCorreo(correo: String): Boolean {
        // Validación básica de formato de email
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Formato de correo inválido", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar que contenga @
        if (!correo.contains("@")) {
            Toast.makeText(this, "El correo debe contener @", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar dominios específicos
        val dominiosValidos = listOf(
            "gmail.com",
            "hotmail.com",
            "outlook.com",
            "yahoo.com",
            "yahoo.es",
            "live.com"
        )

        val dominio = correo.substringAfter("@").lowercase()
        if (!dominiosValidos.contains(dominio)) {
            Toast.makeText(this, "Solo se permiten correos de: ${dominiosValidos.joinToString(", ")}", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun validarTelefono(telefono: String): Boolean {
        // Eliminar espacios, guiones y paréntesis
        val telefonoLimpio = telefono.replace(Regex("[\\s\\-()]+"), "")

        // Validar que solo contenga números
        if (!telefonoLimpio.matches(Regex("\\d+"))) {
            Toast.makeText(this, "El teléfono solo puede contener números, espacios, guiones y paréntesis", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar longitud (8 dígitos mínimo, 15 máximo según estándares internacionales)
        if (telefonoLimpio.length < 8 || telefonoLimpio.length > 15) {
            Toast.makeText(this, "El teléfono debe tener entre 8 y 15 dígitos", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}