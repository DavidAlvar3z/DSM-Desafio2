package com.example.dsm_desafio_02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoVentaAdapter(
    private val productos: List<ProductoVenta>,
    private val onEliminar: (ProductoVenta) -> Unit
) : RecyclerView.Adapter<ProductoVentaAdapter.ProductoVentaViewHolder>() {

    class ProductoVentaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombreProductoVenta)
        val cantidad: TextView = view.findViewById(R.id.tvCantidadProductoVenta)
        val precio: TextView = view.findViewById(R.id.tvPrecioProductoVenta)
        val subtotal: TextView = view.findViewById(R.id.tvSubtotalProductoVenta)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarProductoVenta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoVentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_venta, parent, false)
        return ProductoVentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoVentaViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombre.text = producto.nombre
        holder.cantidad.text = "Cant: ${producto.cantidad}"
        holder.precio.text = "${String.format("%.2f", producto.precioUnitario)}"
        holder.subtotal.text = "${String.format("%.2f", producto.cantidad * producto.precioUnitario)}"

        holder.btnEliminar.setOnClickListener { onEliminar(producto) }
    }

    override fun getItemCount() = productos.size
}