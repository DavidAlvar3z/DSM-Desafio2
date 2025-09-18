package com.example.dsm_desafio_02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private var productos: List<Producto>,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.textNombre)
        val precio: TextView = view.findViewById(R.id.textPrecio)
        val stock: TextView = view.findViewById(R.id.textStock)
        val botonEditar: Button = view.findViewById(R.id.btnEditar)
        val botonEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombre.text = producto.nombre
        holder.precio.text = "Precio: ${producto.precio}"
        holder.stock.text = "Stock: ${producto.stock}"

        holder.botonEditar.setOnClickListener { onEditar(producto) }
        holder.botonEliminar.setOnClickListener { onEliminar(producto) }
    }

    override fun getItemCount(): Int = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos = nuevaLista
        notifyDataSetChanged()
    }
}