package com.example.dsm_desafio_02

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object VentaController {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private fun getUserRef() = auth.currentUser?.uid?.let {
        database.child("ventas").child(it)
    }

    private fun getProductosRef() = auth.currentUser?.uid?.let {
        database.child("productos").child(it)
    }

    fun registrarVenta(venta: Venta, onResult: (Boolean) -> Unit) {
        val userRef = getUserRef() ?: return onResult(false)
        val productosRef = getProductosRef() ?: return onResult(false)

        val key = userRef.push().key ?: return onResult(false)
        venta.id = key

        // Primero guardamos la venta
        userRef.child(key).setValue(venta)
            .addOnSuccessListener {
                // Si la venta se guardó exitosamente, actualizamos el stock
                actualizarStockProductos(venta.productos, productosRef) { stockActualizado ->
                    if (!stockActualizado) {
                        // Si falló la actualización del stock, eliminamos la venta
                        userRef.child(key).removeValue()
                    }
                    onResult(stockActualizado)
                }
            }
            .addOnFailureListener { onResult(false) }
    }

    private fun actualizarStockProductos(productos: List<ProductoVenta>, productosRef: com.google.firebase.database.DatabaseReference, onResult: (Boolean) -> Unit) {
        var productosActualizados = 0
        val totalProductos = productos.size
        var huboError = false

        if (totalProductos == 0) {
            onResult(true)
            return
        }

        productos.forEach { productoVenta ->
            // Obtener el producto actual de la base de datos
            productosRef.child(productoVenta.productoId).get()
                .addOnSuccessListener { snapshot ->
                    val producto = snapshot.getValue(Producto::class.java)
                    if (producto != null) {
                        // Calcular nuevo stock
                        val nuevoStock = producto.stock - productoVenta.cantidad

                        // Validar que no sea stock negativo
                        if (nuevoStock >= 0) {
                            // Actualizar el stock en la base de datos
                            productosRef.child(productoVenta.productoId)
                                .child("stock")
                                .setValue(nuevoStock)
                                .addOnSuccessListener {
                                    productosActualizados++
                                    if (productosActualizados == totalProductos && !huboError) {
                                        onResult(true)
                                    }
                                }
                                .addOnFailureListener {
                                    huboError = true
                                    onResult(false)
                                }
                        } else {
                            huboError = true
                            onResult(false)
                        }
                    } else {
                        huboError = true
                        onResult(false)
                    }
                }
                .addOnFailureListener {
                    huboError = true
                    onResult(false)
                }
        }
    }

    fun obtenerVentas(onResult: (List<Venta>) -> Unit) {
        val userRef = getUserRef() ?: return onResult(emptyList())

        userRef.get().addOnSuccessListener { snapshot ->
            val lista = snapshot.children.mapNotNull { it.getValue(Venta::class.java) }
            onResult(lista)
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }
}