# 🏠 Sistema de Gestión Inmobiliaria

![Android](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-red)

## 📋 Descripción
Aplicación móvil integral para la gestión de una empresa inmobiliaria. Permite a los usuarios consultar inmuebles, agentes, zonas y citas. Los administradores cuentan con herramientas exclusivas para **crear, editar y eliminar** registros mediante un control de acceso basado en roles (`is_staff`).

---

## 🚀 Requisitos de Instalación
* **Android Studio:** Versión Jellyfish o superior.
* **JDK:** 17 o superior.
* **Emulador/Dispositivo:** Android API 34+ (UpsideDownCake).
* **Backend:** Tener el servidor Django activo.

---

## ⚙️ Configuración del Backend
Debes apuntar la app a tu servidor local. Asegúrate de configurar la `BASE_URL` en tu archivo de constantes:

// Usa 10.0.2.2 para el emulador de Android refiriéndose al localhost de tu PC
const val BASE_URL = "[http://10.0.2.2:8000/](http://10.0.2.2:8000/)"

## 🔑 Credenciales de Prueba

| Rol | Usuario | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `admin` | `admin` |
| **Usuario** | `francisco` | `F1234567` |

## 🏗️ Entidades Implementadas
La lógica de negocio se organiza en **7 entidades principales**:

*   **Propiedades:** Catálogo de inmuebles con fotos y precios.
*   **Clientes:** Cartera de clientes interesados.
*   **Agentes:** Personal de ventas y sus licencias.
*   **Zonas:** Segmentación geográfica de operaciones.
*   **Citas:** Programación de visitas.
*   **Usuarios:** Control de permisos (`is_staff`).
*   **Auth JWT:** Sistema de seguridad basado en tokens.

## 📱 Listado de Pantallas
*   **Login:** Autenticación segura para el acceso al sistema.
*   **Catálogo:** Lista interactiva y filtrable de propiedades disponibles.
*   **Detalle de Propiedad:** Vista individual con información detallada del inmueble.
*   **Agentes & Clientes:** Gestión centralizada de la base de contactos.
*   **Zonas:** Visualización de áreas y sectores operativos.
*   **Citas:** Gestión y programación organizada de visitas.

---

## 🔌 Ejemplo de Consumo de API
Para realizar operaciones seguras, la aplicación requiere el `Token` obtenido en el login, el cual debe enviarse en el encabezado de cada solicitud:

GET /api/clientes/
HTTP 200 OK
Allow: GET, POST, HEAD, OPTIONS
Content-Type: application/json
Vary: Accept

{
    "count": 1,
    "next": null,
    "previous": null,
    "results": [
        {
            "id": 1,
            "nombre_completo": "Clientes",
            "identificacion": "1725468710",
            "email": "pepito@gmail.com",
            "telefono": "123456789",
            "presupuesto_maximo": "16000.00"
        }
    ]
}

## 🛠️ Instrucciones para Ejecutar

### 1. Backend (Django)
Para levantar el servidor del backend, sigue estos pasos en tu terminal:

```bash
# 1. Clonar el repositorio
git clone [https://github.com/Franciscosoliz/Inmobiliario_app.git](https://github.com/Franciscosoliz/Inmobiliario_app.git)

# 2. Acceder al directorio principal
cd Inmobiliario_app

# 3. Crear y activar el entorno virtual (recomendado)
# En Windows:
python -m venv venv
venv\Scripts\activate
# En macOS/Linux:
python3 -m venv venv
source venv/bin/activate

# 4. Entrar a la carpeta del backend
cd backend_inmobiliario_local

# 5. Instalar dependencias
pip install -r requirements.txt

# 6. Aplicar migraciones
python manage.py migrate

# 7. Ejecutar el servidor
python manage.py runserver