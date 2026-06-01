````md
# Objetos Perdidos UdeA - Backend

Backend del sistema de gestión de objetos perdidos de la Universidad de Antioquia.  
Este proyecto centraliza el registro, publicación, reclamo, entrega y seguimiento de objetos encontrados dentro del campus universitario.

La aplicación busca solucionar el problema de la falta de un sistema centralizado para consultar objetos perdidos en diferentes bloques, facultades o dependencias de la universidad.

---

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Supabase PostgreSQL
- Supabase Auth
- Supabase Storage
- Maven

---

## Arquitectura general

El sistema está dividido en tres partes principales:

```text
Frontend Flutter
        ↓
API REST Spring Boot
        ↓
Base de datos Supabase PostgreSQL
````

Además, Supabase se utiliza para:

```text
Supabase Auth    → Inicio de sesión con Google
Supabase Storage → Almacenamiento de imágenes de objetos
PostgreSQL       → Persistencia de datos
```

El backend concentra la lógica de negocio.
El frontend consume endpoints REST y evita consultar directamente las tablas principales de la base de datos.

---

## Estructura por capas

El backend sigue una arquitectura por capas:

```
Controller → Service → Repository → Database
```

### Controllers

Reciben las peticiones HTTP y delegan la lógica a los servicios.

Ejemplos:

* `ObjetoController`
* `SolicitudReclamoController`
* `ReportePerdidaController`
* `NotificacionController`
* `PersonaController`
* `AuthController`

### Services

Contienen la lógica de negocio del sistema.

Ejemplos:

* `ObjetoService`
* `SolicitudReclamoService`
* `ReportePerdidaService`
* `PersonaService`
* `AuthService`
* `CoincidenciaService`

### Repositories

Gestionan el acceso a la base de datos mediante JPA y consultas SQL nativas.

Ejemplos:

* `ObjetoRepository`
* `SolicitudReclamoRepository`
* `ReportePerdidaRepository`
* `PersonaRepository`
* `PublicacionRepository`
* `NotificacionRepository`

### DTOs y Projections

Se utilizan DTOs para controlar qué información entra y sale de la API.

Ejemplos:

* `ObjetoPublicadoDTO`
* `SolicitudAdminDTO`
* `ReporteAdminDTO`
* `AuthMeDTO`
* `PersonaDTO`
* `CrearObjetoRequest`
* `ActualizarObjetoRequest`
* `EntregarSolicitudRequest`

Las projections permiten mapear consultas complejas que combinan varias tablas.

---

## Módulos funcionales

## 1. Autenticación y perfil

El inicio de sesión se realiza desde el frontend usando Supabase Auth con Google.
El backend valida el token enviado por el frontend en el header:

```
Authorization: Bearer <access_token>
```

Endpoint principal:

```http
GET /api/auth/me
```

Este endpoint permite:

* Validar el token del usuario.
* Verificar que el correo pertenezca al dominio `@udea.edu.co`.
* Consultar o crear la persona en `tbl_persona`.
* Obtener rol, estado y datos básicos del usuario.
* Validar si el perfil está completo.

Endpoint de actualización de perfil:

```http
PUT /api/auth/me/perfil
```

---

## 2. Registro de objetos

Permite que un administrador registre objetos encontrados en la universidad.

Endpoint principal:

```http
POST /api/objetos
```

El administrador puede registrar un objeto con:

* Nombre.
* Descripción general.
* Descripción detallada.
* Categoría.
* Fecha de hallazgo.
* Lugar donde fue encontrado.
* Lugar actual o de custodia.
* Opción de publicar o dejar solo en custodia.

Flujo:

```text
Admin registra objeto
        ↓
Backend crea registro en tbl_objeto
        ↓
Si publicar = true, crea publicación en tbl_publicacion
        ↓
El objeto aparece en la lista pública
```

---

## 3. Imágenes de objetos

Las imágenes se almacenan en Supabase Storage.

Flujo:

```text
Flutter selecciona imagen
        ↓
Flutter sube archivo a Supabase Storage
        ↓
Supabase genera URL pública
        ↓
Flutter envía la URL al backend
        ↓
Backend guarda la URL en tbl_objeto.fotografia
```

Endpoint utilizado para guardar la URL:

```http
PUT /api/objetos/{id}/fotografia
```

De esta forma, el frontend no actualiza directamente la tabla `tbl_objeto`.

---

## 4. Publicaciones

Los objetos publicados son visibles para los estudiantes y pueden ser reclamados.

Endpoints:

```http
GET /api/objetos
PUT /api/objetos/{id}/ocultar-publicacion
```

`GET /api/objetos` retorna únicamente objetos publicados y disponibles.

El administrador puede ocultar una publicación manualmente.
Ocultar una publicación no elimina el objeto, solo cambia el estado de la publicación.

Flujo:

```text
Admin oculta publicación
        ↓
Backend cambia estado de publicación a oculto
        ↓
Objeto desaparece de publicaciones
        ↓
Objeto continúa en inventario
```

---

## 5. Inventario completo

Permite al administrador consultar todos los objetos registrados, sin importar su estado.

Endpoint:

```http
GET /api/objetos/admin
```

Muestra objetos en estados como:

* En custodia.
* Disponible.
* Entregado.
* Donado.
* Desechado.

---

## 6. Detalle de objeto

Permite consultar la información completa de un objeto.

Endpoint:

```http
GET /api/objetos/{id}
```

Retorna información como:

* Nombre.
* Categoría.
* Estado.
* Fotografía.
* Descripción general.
* Descripción detallada.
* Lugar encontrado.
* Lugar actual.
* Fecha de hallazgo.

El frontend muestra información adicional si el usuario es administrador.

---

## 7. Solicitudes de reclamo

Permite que un estudiante reclame un objeto publicado.

Endpoints principales:

```http
POST /api/solicitudes
GET /api/solicitudes/admin
GET /api/solicitudes/usuario/{correo}
PUT /api/solicitudes/{id}/aprobar
PUT /api/solicitudes/{id}/rechazar
PUT /api/solicitudes/{id}/entregar
PUT /api/solicitudes/{id}/anular
```

Flujo de solicitud manual:

```text
Usuario consulta objeto publicado
        ↓
Usuario crea solicitud de reclamo
        ↓
Solicitud queda pendiente
        ↓
Admin aprueba o rechaza
        ↓
Si aprueba, se puede registrar entrega
```

Estados principales de solicitud:

```text
8  = Pendiente
9  = Aprobada
10 = Rechazada
13 = Anulada
2  = Entregada
```

---

## 8. Entrega de objetos

Permite cerrar el proceso cuando un objeto aprobado es entregado al estudiante.

Endpoint:

```http
PUT /api/solicitudes/{id}/entregar
```

Flujo:

```text
Solicitud aprobada
        ↓
Admin registra entrega
        ↓
Backend crea registro de entrega
        ↓
Solicitud pasa a entregada
        ↓
Objeto pasa a entregado
        ↓
Publicación queda oculta
```

---

## 9. Reportes de pérdida

Permite que un estudiante reporte un objeto perdido.

Endpoints:

```http
POST /api/reportes
GET /api/reportes/admin
GET /api/reportes/usuario/{correo}
PUT /api/reportes/{id}/anular
```

Flujo:

```text
Usuario reporta pérdida
        ↓
Reporte queda pendiente
        ↓
Admin revisa reportes
        ↓
Admin puede encontrar una coincidencia
```

Estados de reportes:

```text
6  = Pendiente
7  = Resuelto
22 = Anulado
```

---

## 10. Coincidencias

Permite que el administrador relacione un reporte de pérdida con un objeto ya registrado.

Flujo:

```text
Usuario crea reporte de pérdida
        ↓
Admin encuentra coincidencia con objeto registrado
        ↓
Backend crea solicitud automática
        ↓
Reporte pasa a resuelto
        ↓
Objeto deja de estar disponible
        ↓
Publicación se oculta
        ↓
Usuario recibe notificación
```

Cuando una solicitud viene de una coincidencia, se guarda el campo:

```text
id_reporte
```

Esto permite distinguir:

```text
Solicitud manual       → id_reporte = null
Coincidencia admin     → id_reporte != null
```

---

## 11. Notificaciones internas

El sistema genera notificaciones para los usuarios.

Endpoints:

```http
GET /api/notificaciones/{correo}
PUT /api/notificaciones/{id}/leer
DELETE /api/notificaciones/{id}
DELETE /api/notificaciones/persona/{correo}
```

Funcionalidades:

* Consultar notificaciones del usuario.
* Mostrar notificaciones no leídas.
* Marcar notificaciones como leídas.
* Eliminar una notificación.
* Eliminar todas las notificaciones.
* Mostrar mensaje adicional enviado por el administrador.

---

## 12. Objetos vencidos

Permite gestionar objetos que superaron el tiempo máximo de almacenamiento según su categoría.

Endpoints:

```http
GET /api/objetos/vencidos
PUT /api/objetos/{id}/donar
PUT /api/objetos/{id}/desechar
```

Flujo:

```text
Objeto supera tiempo máximo de almacenamiento
        ↓
Admin lo ve en objetos vencidos
        ↓
Admin decide donar o desechar
        ↓
Backend cambia estado del objeto
        ↓
Publicación queda oculta
```

---

## 13. Directorio de estudiantes

Permite consultar información básica de estudiantes registrados.

Endpoint:

```http
GET /api/personas/estudiantes
```

Retorna datos como:

* Nombre.
* Correo.
* Celular.
* Documento.
* Tipo de documento.
* Estado.
* Rol.

Este módulo evita que Flutter consulte directamente `tbl_persona`.

---

## Estados principales

### Solicitudes

| ID | Estado    |
| -: | --------- |
|  8 | Pendiente |
|  9 | Aprobada  |
| 10 | Rechazada |
| 13 | Anulada   |
|  2 | Entregada |

### Reportes

| ID | Estado    |
| -: | --------- |
|  6 | Pendiente |
|  7 | Resuelto  |
| 22 | Anulado   |

### Publicaciones

| ID | Estado    |
| -: | --------- |
| 11 | Publicada |
| 12 | Oculta    |

---

## Principios y patrones aplicados

### Arquitectura por capas

Se separa la responsabilidad de cada parte del sistema:

```text
Controller → Service → Repository
```

Esto facilita mantenimiento, pruebas y evolución del proyecto.

---

### Controller-Service-Repository

Patrón principal usado en el backend.

Ejemplo:

```text
ObjetoController
        ↓
ObjetoService
        ↓
ObjetoRepository
        ↓
Base de datos
```

---

### DTO

Se usan DTOs para transferir datos entre frontend y backend sin exponer directamente las entidades.

Ventajas:

* Controlar la información enviada.
* Evitar acoplamiento con la estructura interna de la base de datos.
* Adaptar respuestas a las necesidades del frontend.

---

### Repository Pattern

Los repositories centralizan el acceso a datos.

Ventajas:

* Evitan que la lógica SQL esté dispersa.
* Permiten separar lógica de negocio y persistencia.
* Facilitan el mantenimiento del código.

---

### Projections

Se usan projections para consultas que combinan varias tablas.

Ejemplo:

```text
Objeto + Categoría + Estado + Lugar
```

Esto permite retornar datos optimizados sin cargar entidades completas.

---

## Principios SOLID

### Single Responsibility Principle

Cada clase tiene una responsabilidad concreta.

Ejemplos:

* `ObjetoController`: recibe peticiones de objetos.
* `ObjetoService`: maneja reglas de negocio de objetos.
* `ObjetoRepository`: consulta la base de datos.

---

### Open/Closed Principle

Se agregaron funcionalidades sin romper las existentes.

Ejemplos:

* Se agregó ocultamiento manual de publicaciones.
* Se agregó actualización de fotografía por endpoint.
* Se agregó anulación lógica de solicitudes y reportes.

---

### Dependency Inversion Principle

Los servicios dependen de repositories inyectados por Spring, no de implementaciones creadas manualmente.

Ejemplo:

```java
private final ObjetoRepository objetoRepository;
```

---

## Principios GRASP

### Controller

Los controllers reciben las operaciones del sistema y delegan la lógica.

### Information Expert

Cada service contiene la lógica de negocio del módulo que maneja.

### Low Coupling

El frontend ya no depende directamente de las tablas principales de Supabase.

### High Cohesion

Cada módulo agrupa funcionalidades relacionadas:

* Objetos.
* Solicitudes.
* Reportes.
* Notificaciones.
* Personas.
* Autenticación.

### Creator

Los services crean entidades relacionadas cuando corresponde.

Ejemplos:

* `ObjetoService` crea objetos y publicaciones.
* `CoincidenciaService` crea solicitudes automáticas y notificaciones.
* `SolicitudReclamoService` registra entregas.

---

## Decisiones de diseño

### No eliminar registros críticos

El sistema evita borrar físicamente información importante.
En su lugar usa estados como:

```text
Anulado
Resuelto
Entregado
Oculto
Donado
Desechado
```

Esto permite conservar trazabilidad.

---

### Separación entre información pública y privada

Los objetos tienen:

```text
Descripción general   → visible para usuarios
Descripción detallada → visible para administradores
```

Esto ayuda a validar reclamos sin exponer información sensible.

---

### Supabase como proveedor de servicios

Supabase se usa para:

* Base de datos PostgreSQL.
* Autenticación con Google.
* Almacenamiento de imágenes.

La lógica de negocio se maneja desde Spring Boot.

---

## Endpoints principales

### Autenticación

```http
GET /api/auth/me
PUT /api/auth/me/perfil
```

### Objetos

```http
GET /api/objetos
GET /api/objetos/admin
GET /api/objetos/vencidos
GET /api/objetos/{id}
POST /api/objetos
PUT /api/objetos/{id}
PUT /api/objetos/{id}/fotografia
PUT /api/objetos/{id}/ocultar-publicacion
PUT /api/objetos/{id}/donar
PUT /api/objetos/{id}/desechar
```

### Solicitudes

```http
POST /api/solicitudes
GET /api/solicitudes/admin
GET /api/solicitudes/usuario/{correo}
PUT /api/solicitudes/{id}/aprobar
PUT /api/solicitudes/{id}/rechazar
PUT /api/solicitudes/{id}/entregar
PUT /api/solicitudes/{id}/anular
```

### Reportes

```http
POST /api/reportes
GET /api/reportes/admin
GET /api/reportes/usuario/{correo}
PUT /api/reportes/{id}/anular
```

### Personas

```http
GET /api/personas/estudiantes
```

### Notificaciones

```http
GET /api/notificaciones/{correo}
PUT /api/notificaciones/{id}/leer
DELETE /api/notificaciones/{id}
DELETE /api/notificaciones/persona/{correo}
```

### Catálogos

```http
GET /api/categorias
GET /api/lugares
```

---

## Flujo principal de la aplicación

```text
Admin registra objeto
        ↓
Puede publicarlo o dejarlo en custodia
        ↓
Usuario consulta objetos publicados
        ↓
Usuario crea solicitud de reclamo
        ↓
Admin aprueba o rechaza
        ↓
Admin registra entrega
```

Flujo alternativo:

```text
Usuario reporta pérdida
        ↓
Admin encuentra coincidencia
        ↓
Sistema crea solicitud automática
        ↓
Usuario recibe notificación
        ↓
Proceso continúa como reclamo
```

---

## Estado actual del proyecto

El backend permite:

* Registrar objetos.
* Publicar objetos.
* Ocultar publicaciones.
* Consultar inventario completo.
* Consultar detalle de objetos.
* Guardar fotografía mediante endpoint.
* Crear solicitudes de reclamo.
* Aprobar, rechazar, entregar y anular solicitudes.
* Crear y consultar reportes de pérdida.
* Anular reportes pendientes.
* Encontrar coincidencias entre reportes y objetos.
* Generar notificaciones internas.
* Consultar directorio de estudiantes.
* Gestionar objetos vencidos mediante donación o desecho.
* Validar usuario autenticado mediante token de Supabase.

---

## Ejecución local

El backend corre por defecto en:

```
http://localhost:8080
```

Para ejecutar el proyecto:

```bash
mvn spring-boot:run
```

O desde el IDE usando la clase principal de Spring Boot.

---

## Resumen
* Supabase Auth se usa para iniciar sesión con Google.
* Supabase Storage se usa para almacenar imágenes.
* La lógica de negocio se encuentra en el backend.
* Las tablas principales no deben ser modificadas directamente desde Flutter.
* Las imágenes se guardan como URL pública en `tbl_objeto.fotografia`.
* La eliminación física se evita para conservar trazabilidad.

