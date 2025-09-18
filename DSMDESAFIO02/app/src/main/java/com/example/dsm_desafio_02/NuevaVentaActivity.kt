package com.example.dsm_desafio_02

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NuevaVentaActivity : AppCompatActivity() {

    private lateinit var spinnerCliente: Spinner
    private lateinit var recyclerProductos: RecyclerView
    private lateinit var btnAgregarProducto: Button
    private lateinit var tvTotal: TextView
    private lateinit var btnGuardarVenta: Button

    private var listaClientes = mutableListOf<Cliente>()
    private var listaProductos = mutableListOf<Producto>()
    private var productosVenta = mutableListOf<ProductoVenta>()
    private lateinit var adapterProductosVenta: ProductoVentaAdapter
    private var clienteSeleccionado: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_venta)

        inicializarVistas()
        configurarRecyclerView()
        cargarClientes()
        cargarProductos()
        configurarEventos()
    }

    private fun inicializarVistas() {
        spinnerCliente = findViewById(R.id.spinnerCliente)
        recyclerProductos = findViewById(R.id.recyclerProductosVenta)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
        tvTotal = findViewById(R.id.tvTotal)
        btnGuardarVenta = findViewById(R.id.btnGuardarVenta)
    }

    private fun configurarRecyclerView() {
        adapterProductosVenta = ProductoVentaAdapter(productosVenta) { producto ->
            productosVenta.remove(producto)
            adapterProductosVenta.notifyDataSetChanged()
            calcularTotal()
        }
        recyclerProductos.layoutManager = LinearLayoutManager(this)
        recyclerProductos.adapter = adapterProductosVenta
    }

    private fun cargarClientes() {
        ClienteController.obtenerClientes { clientes ->
            listaClientes.clear()
            listaClientes.addAll(clientes)

            val nombres = clientes.map { "${it.nombre} - ${it.correo}" }.toMutableList()
            nombres.add(0, "Seleccionar cliente...")

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCliente.adapter = adapter

            spinnerCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    clienteSeleccionado = if (position > 0) listaClientes[position - 1] else null
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun cargarProductos() {
        ProductoController().obtenerProductos { productos ->
            listaProductos.clear()
            listaProductos.addAll(productos.filter { it.stock > 0 })
        }
    }

    private fun configurarEventos() {
        btnAgregarProducto.setOnClickListener {
            mostrarDialogoAgregarProducto()
        }

        btnGuardarVenta.setOnClickListener {
            guardarVenta()
        }
    }

    private fun mostrarDialogoAgregarProducto() {
        if (listaProductos.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show()
            return
        }

        val nombres = listaProductos.map { "${it.nombre} - $${it.precio} (Stock: ${it.stock})" }

        AlertDialog.Builder(this)
            .setTitle("Seleccionar Producto")
            .setItems(nombres.toTypedArray()) { _, position ->
                val producto = listaProductos[position]
                mostrarDialogoCantidad(producto)
            }
            .show()
    }

    private fun mostrarDialogoCantidad(producto: Producto) {
        val editText = EditText(this)
        editText.hint = "Cantidad (máx: ${producto.stock})"
        editText.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        AlertDialog.Builder(this)
            .setTitle("Cantidad de ${producto.nombre}")
            .setView(editText)
            .setPositiveButton("Agregar") { _, _ ->
                val cantidad = editText.text.toString().toIntOrNull() ?: 0
                if (cantidad > 0 && cantidad <= producto.stock) {
                    agregarProductoAVenta(producto, cantidad)
                } else {
                    Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun agregarProductoAVenta(producto: Producto, cantidad: Int) {
        val productoVenta = ProductoVenta(
            productoId = producto.id ?: "",
            nombre = producto.nombre,
            cantidad = cantidad,
            precioUnitario = producto.precio
        )

        productosVenta.add(productoVenta)
        adapterProductosVenta.notifyDataSetChanged()
        calcularTotal()
    }

    private fun calcularTotal() {
        val total = productosVenta.sumOf { it.cantidad * it.precioUnitario }
        tvTotal.text = "Total: $${String.format("%.2f", total)}"
    }

    private fun guardarVenta() {
        if (clienteSeleccionado == null) {
            Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (productosVenta.isEmpty()) {
            Toast.makeText(this, "Agrega al menos un producto", Toast.LENGTH_SHORT).show()
            return
        }

        val total = productosVenta.sumOf { it.cantidad * it.precioUnitario }
        val venta = Venta(
            clienteId = clienteSeleccionado!!.id,
            productos = productosVenta.toList(),
            total = total,
            fecha = System.currentTimeMillis()
        )

        VentaController.registrarVenta(venta) { exito ->
            if (exito) {
                Toast.makeText(this, "Venta registrada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al registrar la venta", Toast.LENGTH_SHORT).show()
            }
        }
    }
}