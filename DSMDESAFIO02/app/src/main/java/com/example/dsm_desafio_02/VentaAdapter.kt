package com.example.dsm_desafio_02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class VentaAdapter(
    private val listaVentas: List<Venta>,
    private val onClick: (Venta) -> Unit
) : RecyclerView.Adapter<VentaAdapter.VentaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VentaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venta, parent, false)
        return VentaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VentaViewHolder, position: Int) {
        val venta = listaVentas[position]

        holder.tvCliente.text = "Venta #${venta.id.take(8)}"

        holder.tvTotal.text = "${String.format("%.2f", venta.total)}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvFecha.text = dateFormat.format(Date(venta.fecha))

        val cantidadProductos = venta.productos.sumOf { it.cantidad }
        holder.tvProductos?.text = "$cantidadProductos producto(s)"

        holder.itemView.setOnClickListener { onClick(venta) }
    }

    override fun getItemCount(): Int = listaVentas.size

    class VentaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCliente: TextView = itemView.findViewById(R.id.tvClienteVenta)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotalVenta)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaVenta)
        val tvProductos: TextView? = itemView.findViewById(R.id.tvProductosVenta)
    }
}