package com.example.dsm_desafio_02

import java.io.Serializable
data class Producto(
    var id: String? = null,
    var nombre: String = "",
    var precio: Double = 0.0,
    var stock: Int = 0,
    var descripcion: String = ""
) : Serializable