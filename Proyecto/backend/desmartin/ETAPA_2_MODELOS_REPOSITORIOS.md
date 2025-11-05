# 🔧 ETAPA 2: Actualización de Modelos y Repositorios

## ✅ Resumen de Cambios Realizados

### **1. Modelo `ChatSesion`**

**Cambio Principal:**
- ✅ Agregado campo `curso` con relación `@ManyToOne` a `Curso`

**Código Actualizado:**
```java
@ManyToOne
@JoinColumn(name = "id_curso_fk")
private Curso curso;
```

**Validación Implementada:**
- Una sesión es O individual (`idAlumno` != null) O grupal (`idCurso` != null)
- No ambas, no ninguna
- Validación realizada en `ChatService.crearSesion()`

---

### **2. DTOs Actualizados**

#### **`ChatSesionDTO`**
- ✅ Agregado campo `idCurso` para reflejar chats grupales

```java
private Integer idCurso;  // NULL para chats individuales
```

#### **`CrearChatSesionRequest`**
- ✅ Agregado campo `idCurso` para crear chats grupales

```java
private Integer idAlumno;  // NULL para chats grupales
private Integer idCurso;   // NULL para chats individuales
```

---

### **3. Repositorio `ChatSesionRepository`**

**Métodos Agregados:**
```java
// Consultas por alumno (chats individuales)
List<ChatSesion> findByAlumno_IdAlumno(Integer idAlumno);

// Consultas por curso (chats grupales)
List<ChatSesion> findByCurso_IdCurso(Integer idCurso);

// Consultas combinadas: docente y alumno
List<ChatSesion> findByDocente_IdDocenteAndAlumno_IdAlumno(Integer idDocente, Integer idAlumno);

// Consultas combinadas: docente y curso
List<ChatSesion> findByDocente_IdDocenteAndCurso_IdCurso(Integer idDocente, Integer idCurso);
```

**Métodos Existentes (sin cambios):**
- `findByDocente_IdDocente(Integer idDocente)` - ya existía

---

### **4. Servicio `ChatService`**

#### **Método `crearSesion()` - Actualizado**

**Validaciones Agregadas:**
1. ✅ Valida que la sesión es O individual O grupal (no ambas)
2. ✅ Valida que no sea ninguna (ambos NULL)
3. ✅ Valida que el curso existe
4. ✅ Valida que el docente dicta el curso (solo para chats grupales)

**Lógica de Negocio:**
```java
// Configurar como chat individual
if (esIndividual) {
    Alumno alumno = alumnoRepository.findById(request.getIdAlumno())
        .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
    sesion.setAlumno(alumno);
    sesion.setCurso(null);  // Asegurar que es NULL
}

// Configurar como chat grupal
if (esGrupal) {
    Curso curso = cursoRepository.findById(request.getIdCurso())
        .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    
    // Validar que el docente dicta el curso
    if (!curso.getDocente().getIdDocente().equals(docente.getIdDocente())) {
        throw new IllegalArgumentException("El docente no dicta este curso");
    }
    
    sesion.setCurso(curso);
    sesion.setAlumno(null);  // Asegurar que es NULL
}
```

#### **Métodos Actualizados:**
- ✅ `obtenerSesionesPorDocente()` - ahora incluye `idCurso` en el DTO
- ✅ `obtenerSesion()` - ahora incluye `idCurso` en el DTO

#### **Métodos Nuevos:**
- ✅ `obtenerSesionesPorCurso(Integer idCurso)` - lista sesiones de un curso
- ✅ `obtenerSesionesPorAlumno(Integer idAlumno)` - lista sesiones de un alumno

---

## 🔍 Validaciones Implementadas

### **Validación de Tipo de Sesión**

**Regla de Negocio:**
- Una sesión DEBE ser individual O grupal
- NO puede ser ambas
- NO puede ser ninguna

**Implementación:**
```java
boolean esIndividual = request.getIdAlumno() != null;
boolean esGrupal = request.getIdCurso() != null;

if (esIndividual && esGrupal) {
    throw new IllegalArgumentException("Una sesión no puede ser individual y grupal a la vez");
}

if (!esIndividual && !esGrupal) {
    throw new IllegalArgumentException("Una sesión debe ser individual (idAlumno) o grupal (idCurso)");
}
```

### **Validación de Autorización**

**Regla de Negocio:**
- Solo el docente que dicta un curso puede crear chats grupales de ese curso

**Implementación:**
```java
if (esGrupal) {
    Curso curso = cursoRepository.findById(request.getIdCurso())
        .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    
    // Validar que el docente dicta el curso
    if (!curso.getDocente().getIdDocente().equals(docente.getIdDocente())) {
        throw new IllegalArgumentException("El docente no dicta este curso");
    }
}
```

---

## 📊 Estructura de Datos Actualizada

### **Chat Individual (Existente)**
```json
{
  "idSesion": 1,
  "idDocente": 1,
  "idAlumno": 5,
  "idCurso": null,  // NULL para chats individuales
  "tituloSesion": "Consulta sobre Juan",
  "fechaCreacion": "2025-01-XX..."
}
```

### **Chat Grupal (Nuevo)**
```json
{
  "idSesion": 2,
  "idDocente": 1,
  "idAlumno": null,  // NULL para chats grupales
  "idCurso": 2,
  "tituloSesion": "Consulta sobre Matemáticas 1",
  "fechaCreacion": "2025-01-XX..."
}
```

---

## ✅ Validación de Compilación

**Resultado:**
- ✅ Sin errores de compilación
- ✅ Sin warnings de linter
- ✅ Todas las relaciones JPA validadas
- ✅ Backward compatibility mantenida (chats existentes siguen funcionando)

---

## 🔄 Compatibilidad con Versiones Anteriores

**Chats Existentes:**
- ✅ Los chats existentes (solo con `idAlumno`) seguirán funcionando
- ✅ El campo `idCurso` será NULL para chats antiguos
- ✅ No requiere migración de datos existentes

**Nuevos Chats:**
- ✅ Pueden crearse chats individuales (como antes)
- ✅ Pueden crearse chats grupales (nuevo)

---

## 📝 Archivos Modificados

1. ✅ `src/main/java/com/appmartin/desmartin/model/ChatSesion.java`
2. ✅ `src/main/java/com/appmartin/desmartin/dto/ChatSesionDTO.java`
3. ✅ `src/main/java/com/appmartin/desmartin/dto/CrearChatSesionRequest.java`
4. ✅ `src/main/java/com/appmartin/desmartin/repository/ChatSesionRepository.java`
5. ✅ `src/main/java/com/appmartin/desmartin/service/ChatService.java`

---

## 🎯 Próximos Pasos

**ETAPA 2 Completada:**
- ✅ Modelos actualizados con soporte para cursos
- ✅ Repositorios extendidos con métodos de consulta
- ✅ Servicios actualizados con validaciones
- ✅ DTOs actualizados

**Siguiente Etapa:**
- ➡️ **ETAPA 3**: Crear Servicio de IA (OpenRouterClient)
  - Crear `OpenRouterService` como @Service Spring
  - Mover API key a `application.properties`
  - Implementar cliente HTTP para DeepSeek
  - Manejo de errores y retry logic

---

## ⚠️ Notas Importantes

1. **Validación en Código**: La validación de que una sesión es O individual O grupal se hace en Java, ya que MySQL puede no soportar CHECK constraints.

2. **Seguridad**: La validación de que el docente dicta el curso previene acceso no autorizado.

3. **Backward Compatibility**: Los chats existentes seguirán funcionando sin cambios.

4. **Relaciones JPA**: Todas las relaciones están correctamente mapeadas con `@ManyToOne` y `@JoinColumn`.

