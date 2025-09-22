# 🗄️ Configuración de Base de Datos - Proyecto AppMartin

## 📋 Información General

Este proyecto utiliza **MySQL** como base de datos principal para el sistema de gestión académica con IA.

- **Base de datos:** `prmartin`
- **Puerto:** `3306` (por defecto)
- **Charset:** `utf8mb4`
- **Collation:** `utf8mb4_unicode_ci`

## 🚀 Instalación Rápida

### Opción 1: Usando MySQL Workbench (Recomendado)

1. **Abrir MySQL Workbench**
2. **Conectar a tu servidor MySQL local**
3. **Ejecutar el script completo:**
   ```sql
   -- Copiar y pegar todo el contenido de 'scripde labd.sql'
   ```

### Opción 2: Usando línea de comandos

```bash
# Conectar a MySQL
mysql -u root -p

# Ejecutar el script
source /ruta/completa/a/scripde\ labd.sql
```

### Opción 3: Usando phpMyAdmin

1. **Abrir phpMyAdmin** en tu navegador
2. **Crear nueva base de datos:** `prmartin`
3. **Seleccionar la base de datos**
4. **Ir a la pestaña "SQL"**
5. **Copiar y pegar** el contenido de `scripde labd.sql`
6. **Ejecutar**

## 📊 Estructura de la Base de Datos

### Tablas Principales

| Tabla | Descripción | Registros de Prueba |
|-------|-------------|-------------------|
| `Roles` | Roles del sistema (ADMIN, DOCENTE, ALUMNO) | 3 |
| `Usuarios` | Usuarios generales del sistema | 7 |
| `Administradores` | Administradores del sistema | 3 |
| `Docentes` | Información de docentes | 3 |
| `Alumnos` | Información de alumnos | 4 |
| `Cursos` | Cursos disponibles | 5 |
| `Matriculas` | Matrículas de alumnos en cursos | 10 |
| `Calificaciones` | Calificaciones de alumnos | 10 |
| `Auditoria` | Log de acciones del sistema | 0 |
| `SolicitudesIA` | Solicitudes de IA | 3 |
| `RespuestasIA` | Respuestas generadas por IA | 1 |
| `TestsGardner` | Tests de inteligencias múltiples | 2 |

## 🔐 Credenciales de Acceso

### Administradores del Sistema

| Usuario | Contraseña | Email | Rol |
|---------|------------|-------|-----|
| `admin1` | `admin123` | admin1@prmartin.com | Administrador Principal |
| `admin2` | `admin123` | admin2@prmartin.com | Administrador Secundario |
| `admin3` | `admin123` | admin3@prmartin.com | Administrador Terciario |

### Usuarios de Prueba

| Usuario | Contraseña | Email | Rol |
|---------|------------|-------|-----|
| `prof.martinez` | `password123` | martinez@prmartin.com | Docente |
| `prof.garcia` | `password123` | garcia@prmartin.com | Docente |
| `prof.lopez` | `password123` | lopez@prmartin.com | Docente |
| `alumno001` | `password123` | alumno001@prmartin.com | Alumno |
| `alumno002` | `password123` | alumno002@prmartin.com | Alumno |
| `alumno003` | `password123` | alumno003@prmartin.com | Alumno |
| `alumno004` | `password123` | alumno004@prmartin.com | Alumno |

## ⚙️ Configuración de la Aplicación

### Archivo `application.properties`

```properties
# Configuración de la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/prmartin?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tu_contraseña_mysql
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración de Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

### Variables de Entorno (Opcional)

Puedes configurar las credenciales usando variables de entorno:

```bash
# Windows
set MYSQL_USERNAME=root
set MYSQL_PASSWORD=tu_contraseña

# Linux/Mac
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=tu_contraseña
```

## 🔧 Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"

```sql
-- Conectar como root y crear usuario
CREATE USER 'appmartin'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON prmartin.* TO 'appmartin'@'localhost';
FLUSH PRIVILEGES;
```

### Error: "Unknown database 'prmartin'"

```sql
-- Crear la base de datos manualmente
CREATE DATABASE prmartin CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Error de conexión desde la aplicación

1. **Verificar que MySQL esté ejecutándose**
2. **Verificar el puerto (3306)**
3. **Verificar las credenciales en `application.properties`**
4. **Verificar que la base de datos `prmartin` existe**

## 📁 Archivos Importantes

- `scripde labd.sql` - Script principal con esquema y datos
- `schema_completo.sql` - Script completo con documentación
- `README_DATABASE.md` - Esta documentación

## 🚀 Comandos Útiles

### Verificar conexión
```sql
SELECT 'Conexión exitosa' as Status;
SHOW TABLES;
```

### Ver datos de prueba
```sql
SELECT COUNT(*) as TotalUsuarios FROM Usuarios;
SELECT COUNT(*) as TotalCursos FROM Cursos;
SELECT COUNT(*) as TotalMatriculas FROM Matriculas;
```

### Limpiar base de datos (CUIDADO)
```sql
-- ⚠️ SOLO si quieres empezar de cero
DROP DATABASE IF EXISTS prmartin;
-- Luego ejecutar el script nuevamente
```

## 👥 Para el Equipo de Desarrollo

1. **Cada miembro debe ejecutar el script `scripde labd.sql`**
2. **Configurar las credenciales en `application.properties`**
3. **Verificar que la aplicación se conecte correctamente**
4. **Usar los endpoints de prueba para validar la conexión**

## 📞 Soporte

Si tienes problemas con la configuración:

1. **Verificar que MySQL esté instalado y ejecutándose**
2. **Revisar los logs de la aplicación**
3. **Verificar la configuración de red/firewall**
4. **Consultar la documentación de MySQL**

---

**¡Listo!** 🎉 Tu base de datos está configurada y lista para usar con el proyecto AppMartin.
