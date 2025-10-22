#!/bin/bash

# Script de prueba para endpoints POST, PUT, DELETE de AppMartin
# Ejecutar después de iniciar el servidor con: mvn spring-boot:run

BASE_URL="http://localhost:8081"

echo "🧪 INICIANDO PRUEBAS DE ENDPOINTS APP MARTIN"
echo "=============================================="

# Función para mostrar respuestas
show_response() {
    echo "📤 Respuesta:"
    echo "$1" | jq . 2>/dev/null || echo "$1"
    echo ""
}

# 1. Probar login (POST)
echo "1️⃣ Probando LOGIN (POST /api/login)"
echo "-----------------------------------"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/login" \
    -H "Content-Type: application/json" \
    -d '{
        "usuario": "admin",
        "contrasena": "123456"
    }')
show_response "$LOGIN_RESPONSE"

# 2. Crear usuario (POST)
echo "2️⃣ Probando CREAR USUARIO (POST /api/usuarios)"
echo "----------------------------------------------"
CREATE_USER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/usuarios" \
    -H "Content-Type: application/json" \
    -d '{
        "nombreUsuario": "test_user_001",
        "contrasena": "password123",
        "rol": "ALUMNO"
    }')
show_response "$CREATE_USER_RESPONSE"

# Extraer ID del usuario creado
USER_ID=$(echo "$CREATE_USER_RESPONSE" | jq -r '.idUsuario' 2>/dev/null)
if [ "$USER_ID" = "null" ] || [ -z "$USER_ID" ]; then
    echo "❌ No se pudo obtener ID del usuario creado"
    USER_ID=1
fi

# 3. Crear alumno (POST)
echo "3️⃣ Probando CREAR ALUMNO (POST /api/alumnos)"
echo "---------------------------------------------"
CREATE_STUDENT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/alumnos" \
    -H "Content-Type: application/json" \
    -d '{
        "nombreUsuario": "alumno_test_001",
        "correoElectronico": "alumno.test@universidad.edu",
        "anioIngreso": 2024
    }')
show_response "$CREATE_STUDENT_RESPONSE"

# Extraer ID del alumno creado
STUDENT_ID=$(echo "$CREATE_STUDENT_RESPONSE" | jq -r '.idAlumno' 2>/dev/null)
if [ "$STUDENT_ID" = "null" ] || [ -z "$STUDENT_ID" ]; then
    echo "❌ No se pudo obtener ID del alumno creado"
    STUDENT_ID=1
fi

# 4. Actualizar rol de usuario (PUT)
echo "4️⃣ Probando ACTUALIZAR ROL (PUT /api/usuarios/$USER_ID/rol)"
echo "----------------------------------------------------------"
UPDATE_ROLE_RESPONSE=$(curl -s -X PUT "$BASE_URL/api/usuarios/$USER_ID/rol" \
    -H "Content-Type: application/json" \
    -d '{
        "rol": "DOCENTE"
    }')
show_response "$UPDATE_ROLE_RESPONSE"

# 5. Actualizar alumno (PUT)
echo "5️⃣ Probando ACTUALIZAR ALUMNO (PUT /api/alumnos/$STUDENT_ID)"
echo "-----------------------------------------------------------"
UPDATE_STUDENT_RESPONSE=$(curl -s -X PUT "$BASE_URL/api/alumnos/$STUDENT_ID" \
    -H "Content-Type: application/json" \
    -d '{
        "nombreUsuario": "alumno_test_001_updated",
        "correoElectronico": "alumno.updated@universidad.edu",
        "anioIngreso": 2023
    }')
show_response "$UPDATE_STUDENT_RESPONSE"

# 6. Eliminar alumno (soft delete) (DELETE)
echo "6️⃣ Probando ELIMINAR ALUMNO (DELETE /api/alumnos/$STUDENT_ID)"
echo "------------------------------------------------------------"
DELETE_STUDENT_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/alumnos/$STUDENT_ID")
show_response "$DELETE_STUDENT_RESPONSE"

# 7. Eliminar usuario (DELETE)
echo "7️⃣ Probando ELIMINAR USUARIO (DELETE /api/usuarios/$USER_ID)"
echo "----------------------------------------------------------"
DELETE_USER_RESPONSE=$(curl -s -X DELETE "$BASE_URL/api/usuarios/$USER_ID")
show_response "$DELETE_USER_RESPONSE"

# 8. Verificar que los endpoints GET siguen funcionando
echo "8️⃣ Probando ENDPOINTS GET"
echo "-------------------------"
echo "📋 Listando usuarios:"
GET_USERS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/usuarios")
show_response "$GET_USERS_RESPONSE"

echo "📋 Listando alumnos:"
GET_STUDENTS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/alumnos")
show_response "$GET_STUDENTS_RESPONSE"

echo "📋 Obteniendo roles:"
GET_ROLES_RESPONSE=$(curl -s -X GET "$BASE_URL/api/usuarios/roles")
show_response "$GET_ROLES_RESPONSE"

echo "✅ PRUEBAS COMPLETADAS"
echo "====================="
echo "Si todas las respuestas muestran códigos 200/201, los endpoints están funcionando correctamente."
echo "Si hay errores 500, revisar los logs del servidor para más detalles."
