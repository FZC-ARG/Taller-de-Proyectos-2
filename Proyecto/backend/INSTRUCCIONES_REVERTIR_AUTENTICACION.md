# 🔒 Instrucciones para Revertir la Autenticación

## ⚠️ IMPORTANTE: Cambios Realizados para Desactivar Autenticación

Se han realizado los siguientes cambios **TEMPORALES** para permitir el acceso a todas las APIs sin autenticación:

### 📁 Archivos Modificados:

#### 1. **SecurityConfig.java**
- **Ubicación:** `appmartin/src/main/java/com/prsanmartin/appmartin/config/SecurityConfig.java`
- **Cambio:** Se cambió `.anyRequest().authenticated()` por `.anyRequest().permitAll()`
- **Cambio:** Se comentó el filtro JWT: `.addFilterBefore(jwtAuthenticationFilter, ...)`

#### 2. **Controladores con @PreAuthorize comentadas:**
- `TestGardnerController.java`
- `CursosController.java` 
- `AdminUsuarioController.java`
- `AuditoriaController.java`
- `AccesosUsuariosController.java`
- `AdminUserController.java`

---

## 🔄 CÓMO REVERTIR LOS CAMBIOS (Activar Autenticación)

### **PASO 1: Restaurar SecurityConfig.java**

En el archivo `appmartin/src/main/java/com/prsanmartin/appmartin/config/SecurityConfig.java`:

**REEMPLAZAR:**
```java
.authorizeHttpRequests(auth -> auth
    // TEMPORAL: Permitir TODAS las rutas sin autenticación para pruebas
    .anyRequest().permitAll()
    
    // CONFIGURACIÓN ORIGINAL (COMENTADA TEMPORALMENTE):
    // // Endpoints públicos
    // .requestMatchers("/api/auth/**").permitAll()
    // ... resto comentado
)
// TEMPORAL: Desactivar JWT filter para pruebas sin autenticación
// .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

**POR:**
```java
.authorizeHttpRequests(auth -> auth
    // Endpoints públicos
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/test/**").permitAll()
    .requestMatchers("/actuator/health").permitAll()
    .requestMatchers("/actuator/info").permitAll()
    .requestMatchers("/swagger-ui/**").permitAll()
    .requestMatchers("/v3/api-docs/**").permitAll()
    .requestMatchers("/swagger-resources/**").permitAll()
    .requestMatchers("/webjars/**").permitAll()
    .requestMatchers("/").permitAll()
    .requestMatchers("/error").permitAll()
    // Endpoints de administrador requieren autenticación
    .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
    .requestMatchers("/api/docente/**").hasAnyRole("ADMINISTRADOR", "DOCENTE")
    .requestMatchers("/api/alumno/**").hasAnyRole("ADMINISTRADOR", "DOCENTE", "ALUMNO")
    .requestMatchers("/api/auditoria/**").hasRole("ADMINISTRADOR")
    // Actuator endpoints protegidos
    .requestMatchers("/actuator/**").hasRole("ADMINISTRADOR")
    // Todos los demás endpoints requieren autenticación
    .anyRequest().authenticated()
)
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
```

### **PASO 2: Restaurar @PreAuthorize en Controladores**

En cada controlador, **REEMPLAZAR:**
```java
// TEMPORAL: Desactivar autenticación para pruebas
// @PreAuthorize("hasRole('ADMIN')")
```

**POR:**
```java
@PreAuthorize("hasRole('ADMIN')")
```

**Archivos a modificar:**
- `TestGardnerController.java`
- `CursosController.java`
- `AdminUsuarioController.java`
- `AuditoriaController.java`
- `AccesosUsuariosController.java`
- `AdminUserController.java`

### **PASO 3: Verificar que la Autenticación Funciona**

1. **Reiniciar la aplicación**
2. **Probar endpoint de login:**
   ```bash
   POST http://localhost:8081/api/auth/login
   {
     "usuario": "admin",
     "contrasena": "password"
   }
   ```
3. **Usar el token JWT en headers:**
   ```bash
   Authorization: Bearer <token_jwt>
   ```

---

## 🧪 ESTADO ACTUAL: Autenticación DESACTIVADA

### ✅ **Lo que funciona SIN autenticación:**
- Todas las rutas `/api/**` son accesibles sin token
- No se requiere login previo
- Postman puede hacer requests directamente

### ❌ **Lo que NO funciona (por diseño):**
- Validación de roles
- Control de acceso por usuario
- Auditoría de accesos
- Seguridad de endpoints

---

## 🚨 ADVERTENCIAS

1. **NO usar en producción** con autenticación desactivada
2. **Revertir cambios** antes de desplegar a producción
3. **Probar autenticación** después de revertir cambios
4. **Verificar tokens JWT** funcionan correctamente

---

## 📝 COMANDOS ÚTILES

### Para buscar todas las anotaciones @PreAuthorize comentadas:
```bash
grep -r "// @PreAuthorize" appmartin/src/main/java/
```

### Para verificar que la autenticación está activa:
```bash
# Debería devolver 401 Unauthorized
curl -X GET http://localhost:8081/api/admin/administradores
```

### Para verificar que la autenticación está desactivada:
```bash
# Debería devolver 200 OK (sin token)
curl -X GET http://localhost:8081/api/admin/administradores
```

---

**Fecha de modificación:** $(date)  
**Propósito:** Pruebas de funcionalidad sin autenticación  
**Estado:** ⚠️ TEMPORAL - REVERTIR ANTES DE PRODUCCIÓN
