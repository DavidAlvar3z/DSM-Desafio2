package com.example.dsm_desafio_02

data class Venta(
    var id: String = "",
    var clienteId: String = "",
    var productos: List<ProductoVenta> = listOf(),
    var total: Double = 0.0,
    var fecha: Long = System.currentTimeMillis()
)

data class ProductoVenta(
    var productoId: String = "",
    var nombre: String = "",
    var cantidad: Int = 0,
    var precioUnitario: Double = 0.0
)