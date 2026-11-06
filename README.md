# Sistema IoT Cliente-Servidor (INACAP)

Proyecto desarrollado para la asignatura de arquitectura IoT, implementando comunicación bidireccional mediante Sockets TCP y persistencia de datos local con SQLite en Android.

## Arquitectura del Sistema
- **Servidor (`ServidorIoT` - Teléfono Android):** Actúa como receptor principal. Escucha conexiones en el puerto `8888`, procesa las tramas entrantes con formato estructurado (`TEMP:x|PUERTA:y`), actualiza el panel de control visual en tiempo real y almacena los registros de forma persistente en una base de datos SQLite.
- **Cliente (`Cliente` - Tablet Android):** Dispositivo emisor que se conecta a la red local Wi-Fi mediante la dirección IP del servidor, permitiendo ingresar los valores de los sensores y enviar las tramas mediante Sockets TCP.

## Tecnologías Utilizadas
- Kotlin / Android Studio
- Sockets TCP (`java.net`)
- SQLite (Base de datos local en Android)
- Arquitectura Cliente-Servidor en red local Wi-Fi
