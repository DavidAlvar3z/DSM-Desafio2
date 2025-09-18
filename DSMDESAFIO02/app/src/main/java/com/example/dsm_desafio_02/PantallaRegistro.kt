package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase

class PantallaRegistro : AppCompatActivity() {

    private lateinit var autenticacion: FirebaseAuth
    private lateinit var manejadorFacebook: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        manejadorFacebook = CallbackManager.Factory.create()

        setContentView(R.layout.activity_pantalla_registro)

        autenticacion = FirebaseAuth.getInstance()

        val campoCorreo = findViewById<EditText>(R.id.emailInput)
        val campoClave = findViewById<EditText>(R.id.passwordInput)
        val botonRegistro = findViewById<Button>(R.id.registerButton)
        val botonGitHub = findViewById<Button>(R.id.githubButton)
        val botonFacebook = findViewById<Button>(R.id.facebookButton)

        val botonLogin = findViewById<Button>(R.id.goToLoginButton)
        botonLogin.setOnClickListener {
            val intent = Intent(this, PantallaLogin::class.java)
            startActivity(intent)
            finish()
        }

        botonRegistro.setOnClickListener {
            val correo = campoCorreo.text.toString().trim()
            val clave = campoClave.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            autenticacion.createUserWithEmailAndPassword(correo, clave)
                .addOnSuccessListener {
                    guardarUsuario("correo")
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    irAlMenuPrincipal()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        botonGitHub.setOnClickListener {
            autenticacion.signOut()

            val proveedor = OAuthProvider.newBuilder("github.com")
            proveedor.addCustomParameter("prompt", "login")

            autenticacion.startActivityForSignInWithProvider(this, proveedor.build())
                .addOnSuccessListener {
                    guardarUsuario("github")
                    Toast.makeText(this, "GitHub login exitoso", Toast.LENGTH_SHORT).show()
                    irAlMenuPrincipal()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        botonFacebook.setOnClickListener {
            autenticacion.signOut()
            LoginManager.getInstance().logOut()

            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))

            LoginManager.getInstance().registerCallback(
                manejadorFacebook,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(resultado: LoginResult) {
                        val credencial = FacebookAuthProvider.getCredential(resultado.accessToken.token)
                        autenticacion.signInWithCredential(credencial)
                            .addOnSuccessListener {
                                guardarUsuario("facebook")
                                Toast.makeText(this@PantallaRegistro, "Facebook login exitoso", Toast.LENGTH_SHORT).show()
                                irAlMenuPrincipal()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@PantallaRegistro, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }

                    override fun onCancel() {
                        Toast.makeText(this@PantallaRegistro, "Login cancelado", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(this@PantallaRegistro, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    override fun onActivityResult(codigoSolicitud: Int, codigoResultado: Int, datos: android.content.Intent?) {
        super.onActivityResult(codigoSolicitud, codigoResultado, datos)
        manejadorFacebook.onActivityResult(codigoSolicitud, codigoResultado, datos)
    }

    private fun guardarUsuario(proveedor: String) {
        val usuario = autenticacion.currentUser ?: return
        val uid = usuario.uid
        val correo = usuario.email ?: "sin correo"
        val nombre = usuario.displayName ?: "sin nombre"

        val referencia = FirebaseDatabase.getInstance().getReference("usuarios").child(uid)
        val datos = mapOf(
            "correo" to correo,
            "nombre" to nombre,
            "proveedor" to proveedor
        )

        referencia.setValue(datos)
            .addOnSuccessListener {
                inicializarColecciones(uid)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_LONG).show()
            }
    }

    private fun inicializarColecciones(uid: String) {
        val base = FirebaseDatabase.getInstance()

        base.getReference("ventas").child(uid).setValue(null)

        base.getReference("productos").get().addOnSuccessListener {
            if (!it.exists()) base.getReference("productos").setValue(null)
        }

        base.getReference("clientes").get().addOnSuccessListener {
            if (!it.exists()) base.getReference("clientes").setValue(null)
        }
    }

    private fun irAlMenuPrincipal() {
        val intent = Intent(this, MenuPrincipal::class.java)
        startActivity(intent)
        finish()
    }
}