package com.example.dsm_desafio_02

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarEditarProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etStock: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var btnGuardar: Button
    private val controller = ProductoController()
    private var productoExistente: Producto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etStock = findViewById(R.id.etStock)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnGuardar = findViewById(R.id.btnGuardar)

        @Suppress("DEPRECATION")
        productoExistente = intent.getSerializableExtra("producto") as? Producto

        productoExistente?.let {
            etNombre.setText(it.nombre)
            etPrecio.setText(it.precio.toString())
            etStock.setText(it.stock.toString())
            etDescripcion.setText(it.descripcion)
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
            val stock = etStock.text.toString().toIntOrNull() ?: 0
            val descripcion = etDescripcion.text.toString().trim()

            if (nombre.isEmpty() || descripcion.isEmpty() || precio <= 0 || stock < 0) {
                Toast.makeText(this, "Verifica los datos ingresados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val producto = Producto(
                id = productoExistente?.id,
                nombre = nombre,
                precio = precio,
                stock = stock,
                descripcion = descripcion
            )

            if (productoExistente == null) {
                controller.agregarProducto(producto) { exito ->
                    Toast.makeText(this, if (exito) "Producto agregado" else "Error", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                controller.actualizarProducto(producto) { exito ->
                    Toast.makeText(this, if (exito) "Producto actualizado" else "Error", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}