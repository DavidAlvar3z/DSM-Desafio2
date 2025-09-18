package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class VentasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VentaAdapter
    private var listaVentas = mutableListOf<Venta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ventas)

        recyclerView = findViewById(R.id.recyclerVentas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = VentaAdapter(listaVentas) { venta ->
            mostrarDetalleVenta(venta)
        }

        recyclerView.adapter = adapter

        cargarVentas()

        findViewById<FloatingActionButton>(R.id.fabNuevaVenta).setOnClickListener {
            startActivity(Intent(this, NuevaVentaActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        cargarVentas()
    }

    private fun cargarVentas() {
        VentaController.obtenerVentas { ventas ->
            listaVentas.clear()
            listaVentas.addAll(ventas.sortedByDescending { it.fecha })
            adapter.notifyDataSetChanged()

            if (ventas.isEmpty()) {
                Toast.makeText(this, "No hay ventas registradas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDetalleVenta(venta: Venta) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = dateFormat.format(Date(venta.fecha))

        val productos = venta.productos.joinToString("\n") {
            "• ${it.nombre} x${it.cantidad} - ${String.format("%.2f", it.precioUnitario * it.cantidad)}"
        }

        val mensaje = """
            Fecha: $fecha
            Cliente ID: ${venta.clienteId}
            
            Productos:
            $productos
            
            Total: ${String.format("%.2f", venta.total)}
        """.trimIndent()

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Detalle de Venta #${venta.id.take(8)}")
            .setMessage(mensaje)
            .setPositiveButton("Cerrar", null)
            .show()
    }
}