package com.example.dsm_desafio_02

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onItemClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tvNombreCliente)
        val correo: TextView = view.findViewById(R.id.tvCorreoCliente)
        val telefono: TextView = view.findViewById(R.id.tvTelefonoCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]

        // Asignar los datos reales del cliente a las vistas
        holder.nombre.text = cliente.nombre
        holder.correo.text = cliente.correo
        holder.telefono.text = cliente.telefono

        // Configurar click listener
        holder.itemView.setOnClickListener {
            onItemClick(cliente)
        }
    }

    override fun getItemCount() = clientes.size
}