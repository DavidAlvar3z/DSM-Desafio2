package com.example.dsm_desafio_02

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.*
class PantallaLogin : AppCompatActivity() {

    private lateinit var autenticacion: FirebaseAuth
    private lateinit var manejadorFacebook: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        manejadorFacebook = CallbackManager.Factory.create()

        setContentView(R.layout.activity_pantalla_login)

        autenticacion = FirebaseAuth.getInstance()

        val campoCorreo = findViewById<EditText>(R.id.emailInput)
        val campoClave = findViewById<EditText>(R.id.passwordInput)
        val botonLogin = findViewById<Button>(R.id.loginButton)
        val botonGitHub = findViewById<Button>(R.id.githubButton)
        val botonFacebook = findViewById<Button>(R.id.facebookButton)
        val botonRegistro = findViewById<Button>(R.id.goToRegisterButton) // Nuevo botón

        // Login con Email/Password
        botonLogin.setOnClickListener {
            val correo = campoCorreo.text.toString().trim()
            val clave = campoClave.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            autenticacion.signInWithEmailAndPassword(correo, clave)
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ Login exitoso", Toast.LENGTH_SHORT).show()
                    irAlMenuPrincipal()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "❌ Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Login con GitHub
        botonGitHub.setOnClickListener {
            autenticacion.signOut()
            val proveedor = OAuthProvider.newBuilder("github.com")
            proveedor.addCustomParameter("prompt", "login")
            autenticacion.startActivityForSignInWithProvider(this, proveedor.build())
                .addOnSuccessListener {
                    Toast.makeText(this, "✅ GitHub login exitoso", Toast.LENGTH_SHORT).show()
                    irAlMenuPrincipal()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "❌ Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }

        // Login con Facebook
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
                                Toast.makeText(this@PantallaLogin, "✅ Facebook login exitoso", Toast.LENGTH_SHORT).show()
                                irAlMenuPrincipal()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@PantallaLogin, "❌ Error: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    }

                    override fun onCancel() {
                        Toast.makeText(this@PantallaLogin, "Login cancelado", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(this@PantallaLogin, "❌ Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        // Botón para ir a registro
        botonRegistro.setOnClickListener {
            val intent = Intent(this, PantallaRegistro::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(codigoSolicitud: Int, codigoResultado: Int, datos: Intent?) {
        super.onActivityResult(codigoSolicitud, codigoResultado, datos)
        manejadorFacebook.onActivityResult(codigoSolicitud, codigoResultado, datos)
    }

    private fun irAlMenuPrincipal() {
        val intent = Intent(this, MenuPrincipal::class.java)
        startActivity(intent)
        finish()
    }
}
