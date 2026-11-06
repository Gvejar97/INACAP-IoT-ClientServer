package com.example.servidoriot

import android.content.ContentValues
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvTemperatura: TextView
    private lateinit var tvEstadoPuerta: TextView
    private lateinit var tvIpServidor: TextView
    private lateinit var btnSimularEnvio: Button

    private val puerto = 8888
    private var servidorActivo = true
    private var serverSocket: ServerSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        tvTemperatura = findViewById(R.id.tvTemperatura)
        tvEstadoPuerta = findViewById(R.id.tvEstadoPuerta)
        tvIpServidor = findViewById(R.id.tvIpServidor)
        btnSimularEnvio = findViewById(R.id.btnSimularEnvio)

        mostrarIpLocal()

        // Botón de simulación para pruebas locales
        btnSimularEnvio.setOnClickListener {
            procesarMensajeIoT("TEMP:25.0|PUERTA:Abierta")
        }

        window.decorView.post {
            iniciarServidorTcp()
        }
    }

    private fun mostrarIpLocal() {
        try {
            val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val ipInt = wifiManager.connectionInfo.ipAddress
            if (ipInt != 0) {
                val ipString = String.format(
                    Locale.getDefault(),
                    "%d.%d.%d.%d",
                    ipInt and 0xff,
                    ipInt shr 8 and 0xff,
                    ipInt shr 16 and 0xff,
                    ipInt shr 24 and 0xff
                )
                tvIpServidor.text = "IP Servidor: $ipString"
            } else {
                tvIpServidor.text = "IP Servidor: Conectado por Datos/Hotspot"
            }
        } catch (e: Exception) {
            tvIpServidor.text = "IP Servidor: 127.0.0.1 (Local)"
        }
    }

    private fun iniciarServidorTcp() {
        Thread {
            try {
                serverSocket?.close()

                serverSocket = ServerSocket().apply {
                    reuseAddress = true
                    bind(InetSocketAddress(puerto))
                }

                runOnUiThread {
                    tvEstadoPuerta.text = "Escuchando nodo (Puerto 8888)..."
                }

                while (servidorActivo) {
                    try {
                        val socket = serverSocket ?: break
                        val clientSocket = socket.accept()
                        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                        val mensaje = reader.readLine()

                        if (mensaje != null) {
                            runOnUiThread {
                                procesarMensajeIoT(mensaje)
                            }
                        }
                        clientSocket.close()
                    } catch (e: Exception) {
                        if (!servidorActivo) break
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvEstadoPuerta.text = "Error: Puerto ocupado o sin permisos"
                }
            }
        }.start()
    }

    private fun procesarMensajeIoT(data: String) {
        try {
            var tempVal = ""
            var puertaVal = ""
            val partes = data.split("|")

            for (parte in partes) {
                if (parte.startsWith("TEMP:")) {
                    tempVal = parte.substring(5)
                    tvTemperatura.text = "$tempVal °C"
                } else if (parte.startsWith("PUERTA:")) {
                    puertaVal = parte.substring(7)
                    tvEstadoPuerta.text = puertaVal
                }
            }

            // Almacenamiento local obligatorio en SQLite (DatabaseHelper)
            val dbHelper = DatabaseHelper(this)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("temperatura", tempVal)
                put("puerta", puertaVal)
            }
            db.insert("registros", null, values)
            db.close()

        } catch (e: Exception) {
            tvEstadoPuerta.text = data
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        servidorActivo = false
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            // Ignorar
        }
    }
}