@echo off
echo ========================================
echo    CONFIGURACION DE BASE DE DATOS
echo    Proyecto AppMartin - Taller Proyectos 2
echo ========================================
echo.

echo Verificando conexion a MySQL...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: MySQL no esta instalado o no esta en el PATH
    echo Por favor instala MySQL y agrega al PATH del sistema
    pause
    exit /b 1
)

echo MySQL encontrado. Continuando...
echo.

echo Ingresa la contraseña de MySQL (root):
set /p mysql_password=

echo.
echo Creando base de datos y tablas...
mysql -u root -p%mysql_password% < "scripde labd.sql"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo    BASE DE DATOS CONFIGURADA EXITOSAMENTE
    echo ========================================
    echo.
    echo Credenciales de administrador:
    echo - Usuario: admin1, admin2, admin3
    echo - Contraseña: admin123
    echo.
    echo Usuarios de prueba:
    echo - Docentes: prof.martinez, prof.garcia, prof.lopez
    echo - Alumnos: alumno001, alumno002, alumno003, alumno004
    echo - Contraseña: password123
    echo.
    echo La aplicacion Spring Boot deberia conectarse automaticamente.
    echo.
) else (
    echo.
    echo ERROR: No se pudo configurar la base de datos
    echo Verifica que:
    echo 1. MySQL este ejecutandose
    echo 2. La contraseña sea correcta
    echo 3. Tengas permisos de administrador
    echo.
)

pause
