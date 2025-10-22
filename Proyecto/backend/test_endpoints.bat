@echo off
REM Script de prueba para endpoints POST, PUT, DELETE de AppMartin
REM Ejecutar después de iniciar el servidor con: mvn spring-boot:run

set BASE_URL=http://localhost:8081

echo 🧪 INICIANDO PRUEBAS DE ENDPOINTS APP MARTIN
echo ==============================================

REM 1. Probar login (POST)
echo 1️⃣ Probando LOGIN (POST /api/login)
echo -----------------------------------
curl -s -X POST "%BASE_URL%/api/login" ^
    -H "Content-Type: application/json" ^
    -d "{\"usuario\": \"admin\", \"contrasena\": \"123456\"}"
echo.
echo.

REM 2. Crear usuario (POST)
echo 2️⃣ Probando CREAR USUARIO (POST /api/usuarios)
echo ----------------------------------------------
curl -s -X POST "%BASE_URL%/api/usuarios" ^
    -H "Content-Type: application/json" ^
    -d "{\"nombreUsuario\": \"test_user_001\", \"contrasena\": \"password123\", \"rol\": \"ALUMNO\"}"
echo.
echo.

REM 3. Crear alumno (POST)
echo 3️⃣ Probando CREAR ALUMNO (POST /api/alumnos)
echo --------------------------------------------
curl -s -X POST "%BASE_URL%/api/alumnos" ^
    -H "Content-Type: application/json" ^
    -d "{\"nombreUsuario\": \"alumno_test_001\", \"correoElectronico\": \"alumno.test@universidad.edu\", \"anioIngreso\": 2024}"
echo.
echo.

REM 4. Actualizar rol de usuario (PUT) - usando ID 1 por defecto
echo 4️⃣ Probando ACTUALIZAR ROL (PUT /api/usuarios/1/rol)
echo ----------------------------------------------------
curl -s -X PUT "%BASE_URL%/api/usuarios/1/rol" ^
    -H "Content-Type: application/json" ^
    -d "{\"rol\": \"DOCENTE\"}"
echo.
echo.

REM 5. Actualizar alumno (PUT) - usando ID 1 por defecto
echo 5️⃣ Probando ACTUALIZAR ALUMNO (PUT /api/alumnos/1)
echo --------------------------------------------------
curl -s -X PUT "%BASE_URL%/api/alumnos/1" ^
    -H "Content-Type: application/json" ^
    -d "{\"nombreUsuario\": \"alumno_test_001_updated\", \"correoElectronico\": \"alumno.updated@universidad.edu\", \"anioIngreso\": 2023}"
echo.
echo.

REM 6. Eliminar alumno (soft delete) (DELETE)
echo 6️⃣ Probando ELIMINAR ALUMNO (DELETE /api/alumnos/1)
echo --------------------------------------------------
curl -s -X DELETE "%BASE_URL%/api/alumnos/1"
echo.
echo.

REM 7. Eliminar usuario (DELETE)
echo 7️⃣ Probando ELIMINAR USUARIO (DELETE /api/usuarios/1)
echo -----------------------------------------------------
curl -s -X DELETE "%BASE_URL%/api/usuarios/1"
echo.
echo.

REM 8. Verificar que los endpoints GET siguen funcionando
echo 8️⃣ Probando ENDPOINTS GET
echo -------------------------
echo 📋 Listando usuarios:
curl -s -X GET "%BASE_URL%/api/usuarios"
echo.
echo.

echo 📋 Listando alumnos:
curl -s -X GET "%BASE_URL%/api/alumnos"
echo.
echo.

echo 📋 Obteniendo roles:
curl -s -X GET "%BASE_URL%/api/usuarios/roles"
echo.
echo.

echo ✅ PRUEBAS COMPLETADAS
echo =====================
echo Si todas las respuestas muestran códigos 200/201, los endpoints están funcionando correctamente.
echo Si hay errores 500, revisar los logs del servidor para más detalles.

pause
