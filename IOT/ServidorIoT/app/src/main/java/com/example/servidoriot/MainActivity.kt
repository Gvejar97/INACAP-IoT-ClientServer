package com.example.servidoriot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnIniciarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enlace exacto con los IDs de tu activity_main.xml que muestran las capturas
        etUsuario = findViewById(R.id.etUsuario)
        etContrasena = findViewById(R.id.etContrasena)
        btnIniciarSesion = findViewById(R.id.btnIngresar) // Asegúrate que tu botón en el XML tenga este ID o cámbialo aquí

        btnIniciarSesion.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etContrasena.text.toString().trim()

            if (usuario.isNotEmpty() && password.isNotEmpty()) {
                // Transición limpia hacia el Dashboard del Servidor TCP
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}