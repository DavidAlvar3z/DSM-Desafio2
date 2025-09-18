package com.example.dsm_desafio_02

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object ClienteController {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private fun getUserRef() = auth.currentUser?.uid?.let {
        database.child("clientes").child(it)
    }

    fun agregarCliente(nombre: String, correo: String, telefono: String, onResult: (Boolean) -> Unit) {
        val userRef = getUserRef() ?: return onResult(false)

        val key = userRef.push().key ?: return onResult(false)
        val cliente = Cliente(
            id = key,
            nombre = nombre,
            correo = correo,
            telefono = telefono
        )

        userRef.child(key).setValue(cliente)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun obtenerClientes(onResult: (List<Cliente>) -> Unit) {
        val userRef = getUserRef() ?: return onResult(emptyList())

        userRef.get().addOnSuccessListener { snapshot ->
            val lista = snapshot.children.mapNotNull { it.getValue(Cliente::class.java) }
            onResult(lista)
        }
    }
}