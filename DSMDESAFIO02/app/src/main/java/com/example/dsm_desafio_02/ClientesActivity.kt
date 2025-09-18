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

        recyclerView = findViewById(R.id.recyclerClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ClienteAdapter(listaClientes) { cliente ->

        }
        recyclerView.adapter = adapter

        ClienteController.obtenerClientes { clientes ->
            listaClientes.clear()
            listaClientes.addAll(clientes)
            adapter.notifyDataSetChanged()
        }

        findViewById<FloatingActionButton>(R.id.fabAgregarCliente).setOnClickListener {
            startActivity(Intent(this, FormularioClienteActivity::class.java))
        }
    }
}