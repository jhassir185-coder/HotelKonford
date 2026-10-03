# Control de Asistencia - Hotel Konford

Guía directa para configurar y ejecutar el sistema de asistencia.

## 1. Iniciar el Servidor
Ejecuta el siguiente comando en la raíz del proyecto para encender el servidor:
```bash
mvn spring-boot:run
```

---

## 2. Registrar Asistencia
El registro de asistencia se realiza desde el panel del trabajador (`/trabajador/dashboard`), mediante dos métodos:

* **Escaneo de QR:** el trabajador escanea con la cámara el código QR oficial del hotel (disponible en el panel de administrador).
* **Marcado por DNI:** el trabajador ingresa su número de DNI manualmente.

---

## 3. Acceso
* **Pantalla de Login:** [http://localhost:8080/](http://localhost:8080/)
