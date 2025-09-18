package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private var listaClientes = mutableListOf<Cliente>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        inicializarVistas()
        cargarClientes()
        configurarEventos()
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista cada vez que se regrese a esta activity
        cargarClientes()
    }

    private fun inicializarVistas() {
        recyclerView = findViewById(R.id.recyclerClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ClienteAdapter(listaClientes) { cliente ->
            // Aquí puedes agregar funcionalidad para editar cliente en el futuro
            mostrarDetalleCliente(cliente)
        }

        recyclerView.adapter = adapter
    }

    private fun configurarEventos() {
        findViewById<FloatingActionButton>(R.id.fabAgregarCliente).setOnClickListener {
            val intent = Intent(this, FormularioClienteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarClientes() {
        ClienteController.obtenerClientes { clientes ->
            listaClientes.clear()
            listaClientes.addAll(clientes)
            adapter.notifyDataSetChanged()

            // Opcional: mostrar mensaje si no hay clientes
            if (clientes.isEmpty()) {
                // Toast.makeText(this, "No hay clientes registrados", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDetalleCliente(cliente: Cliente) {
        // Mostrar información completa del cliente
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Información del Cliente")
            .setMessage("""
                Nombre: ${cliente.nombre}
                Correo: ${cliente.correo}
                Teléfono: ${cliente.telefono}
                ID: ${cliente.id.take(8)}...
            """.trimIndent())
            .setPositiveButton("Cerrar", null)
            .show()
    }
}