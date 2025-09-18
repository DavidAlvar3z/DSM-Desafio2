MADTEK - Sistema de Gestión de Ventas

[Android Badge] https://img.shields.io/badge/Platform-Android-green
[Kotlin Badge] https://img.shields.io/badge/Language-Kotlin-blue
[Firebase Badge] https://img.shields.io/badge/Backend-Firebase-orange
[Material Design Badge] https://img.shields.io/badge/UI-Material_Design-purple
[License Badge] https://img.shields.io/badge/License-Academic-red
[University Badge] https://img.shields.io/badge/University-Don%20Bosco-lightgrey

INFORMACIÓN DEL PROYECTO
- Universidad: Don Bosco
- Materia: Desarrollo de Software para Móviles (DSM)
- Desafío: 02
- Aplicación: MADTEK
- Año Académico: 2025

DESCRIPCIÓN
MADTEK es una aplicación Android en Kotlin que permite gestionar productos, clientes y ventas con autenticación multi-plataforma y Firebase Realtime Database. Usa MVC para separar lógica, interfaz y datos, asegurando escalabilidad y fácil mantenimiento.

CARACTERÍSTICAS PRINCIPALES
- Autenticación: Email/Password, GitHub OAuth, Facebook Login
- Inventario: CRUD de productos, control de stock en tiempo real
- Clientes: Registro y gestión de clientes
- Ventas: Procesamiento de transacciones, actualización automática de stock, historial completo
- Seguridad: Datos segmentados por usuario autenticado
- UI: Material Design con RecyclerView y CardView

TECNOLOGÍAS
- Lenguaje: Kotlin
- Plataforma: Android API 28+
- Backend: Firebase (Auth + Realtime Database)
- UI: Material Design
- Autenticación: Firebase Auth, GitHub OAuth, Facebook SDK

ESTRUCTURA DEL PROYECTO
com.example.dsm_desafio_02/
├── activities/      # Pantallas de la app (login, productos, clientes, ventas)
├── adapters/        # Adaptadores de RecyclerView
├── controllers/     # Lógica de negocio y comunicación con Firebase
├── models/          # Clases de datos (Producto, Cliente, Venta)
└── MainActivity.kt

INSTALACIÓN RÁPIDA
git clone https://github.com/DavidAlvar3z/DSM-Desafio2.git
cd DSM-Desafio2
# Abrir en Android Studio y configurar Firebase

1. Crear proyecto en Firebase
2. Habilitar Auth (Email, GitHub, Facebook)
3. Configurar Realtime Database
4. Descargar google-services.json y colocar en app/
5. Configurar Facebook App ID y Client Token en strings.xml

USO
1. Iniciar sesión con Email, GitHub o Facebook
2. Navegar entre módulos: Productos, Clientes, Ventas
3. Crear, editar y eliminar productos y clientes
4. Procesar ventas con cálculo automático y actualización de stock

LICENCIA
Proyecto académico para la Universidad Don Bosco. Todos los derechos reservados a los autores.

CONTACTO
- GitHub: https://github.com/DavidAlvar3z
