package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuPrincipal : AppCompatActivity() {

    private lateinit var botonProductos: Button
    private lateinit var botonClientes: Button
    private lateinit var botonVentas: Button
    private lateinit var botonCerrarSesion: Button
    private lateinit var autenticacion: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)

        autenticacion = FirebaseAuth.getInstance()

        botonProductos = findViewById(R.id.botonProductos)
        botonClientes = findViewById(R.id.botonClientes)
        botonVentas = findViewById(R.id.botonVentas)
        botonCerrarSesion = findViewById(R.id.botonCerrarSesion)

        botonProductos.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }

        botonClientes.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }

        botonVentas.setOnClickListener {
            val intent = Intent(this, VentasActivity::class.java)
            startActivity(intent)
        }

        botonCerrarSesion.setOnClickListener {
            autenticacion.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PantallaRegistro::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}