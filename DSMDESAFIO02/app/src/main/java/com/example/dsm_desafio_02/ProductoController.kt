package com.example.dsm_desafio_02

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProductoController {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private fun getUserRef() = auth.currentUser?.uid?.let {
        database.child("productos").child(it)
    }

    fun agregarProducto(producto: Producto, callback: (Boolean) -> Unit) {
        val userRef = getUserRef() ?: return callback(false)

        val id = userRef.push().key
        producto.id = id
        if (id != null) {
            userRef.child(id).setValue(producto)
                .addOnCompleteListener { callback(it.isSuccessful) }
        } else {
            callback(false)
        }
    }

    fun obtenerProductos(callback: (List<Producto>) -> Unit) {
        val userRef = getUserRef() ?: return callback(emptyList())

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Producto>()
                for (item in snapshot.children) {
                    val producto = item.getValue(Producto::class.java)
                    if (producto != null) lista.add(producto)
                }
                callback(lista)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }

    fun actualizarProducto(producto: Producto, callback: (Boolean) -> Unit) {
        val userRef = getUserRef() ?: return callback(false)

        producto.id?.let { id ->
            userRef.child(id).setValue(producto)
                .addOnCompleteListener { callback(it.isSuccessful) }
        } ?: callback(false)
    }

    fun eliminarProducto(id: String, callback: (Boolean) -> Unit) {
        val userRef = getUserRef() ?: return callback(false)

        userRef.child(id).removeValue()
            .addOnCompleteListener { callback(it.isSuccessful) }
    }
}