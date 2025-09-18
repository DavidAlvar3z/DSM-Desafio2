package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var botonAgregar: Button
    private lateinit var adapter: ProductoAdapter
    private val controller = ProductoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        recyclerView = findViewById(R.id.recyclerProductos)
        botonAgregar = findViewById(R.id.btnAgregar)

        adapter = ProductoAdapter(emptyList(), onEditar = { producto ->
            val intent = Intent(this, AgregarEditarProductoActivity::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
        }, onEliminar = { producto ->
            producto.id?.let { controller.eliminarProducto(it) { } }
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        botonAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarEditarProductoActivity::class.java))
        }

        controller.obtenerProductos { lista ->
            adapter.actualizarLista(lista)
        }
    }
}