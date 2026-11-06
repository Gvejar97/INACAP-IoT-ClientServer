package com.example.clienteiot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var etIpServidor: EditText
    private lateinit var etTemperatura: EditText
    private lateinit var etPuerta: EditText
    private lateinit var btnEnviar: Button
    private lateinit var tvEstadoEnvio: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etIpServidor = findViewById(R.id.etIpServidor)
        etTemperatura = findViewById(R.id.etTemperatura)
        etPuerta = findViewById(R.id.etPuerta)
        btnEnviar = findViewById(R.id.btnEnviar)
        tvEstadoEnvio = findViewById(R.id.tvEstadoEnvio)

        btnEnviar.setOnClickListener {
            val ip = etIpServidor.text.toString().trim()
            val temp = etTemperatura.text.toString().trim()
            val puerta = etPuerta.text.toString().trim()

            if (ip.isNotEmpty() && temp.isNotEmpty() && puerta.isNotEmpty()) {
                enviarTramaAlServidor(ip, "TEMP:$temp|PUERTA:$puerta")
            } else {
                tvEstadoEnvio.text = "Por favor completa todos los campos"
            }
        }
    }

    private fun enviarTramaAlServidor(serverIp: String, mensaje: String) {
        Thread {
            try {
                // Conexión TCP al puerto 8888 del teléfono
                val socket = Socket(serverIp, 8888)
                val writer = PrintWriter(socket.getOutputStream(), true)

                writer.println(mensaje)

                writer.close()
                socket.close()

                runOnUiThread {
                    tvEstadoEnvio.text = "¡Trama enviada con éxito al Servidor!"
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvEstadoEnvio.text = "Error de conexión: ${e.message}"
                }
            }
        }.start()
    }
}