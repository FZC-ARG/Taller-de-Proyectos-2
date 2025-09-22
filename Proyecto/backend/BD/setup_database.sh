#!/bin/bash

echo "========================================"
echo "   CONFIGURACION DE BASE DE DATOS"
echo "   Proyecto AppMartin - Taller Proyectos 2"
echo "========================================"
echo

# Verificar si MySQL está instalado
if ! command -v mysql &> /dev/null; then
    echo "ERROR: MySQL no está instalado o no está en el PATH"
    echo "Por favor instala MySQL:"
    echo "  Ubuntu/Debian: sudo apt-get install mysql-server"
    echo "  CentOS/RHEL: sudo yum install mysql-server"
    echo "  macOS: brew install mysql"
    exit 1
fi

echo "MySQL encontrado. Continuando..."
echo

# Solicitar contraseña de MySQL
read -s -p "Ingresa la contraseña de MySQL (root): " mysql_password
echo

# Verificar conexión
echo "Verificando conexión a MySQL..."
mysql -u root -p$mysql_password -e "SELECT 1;" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "ERROR: No se pudo conectar a MySQL"
    echo "Verifica que:"
    echo "1. MySQL esté ejecutándose"
    echo "2. La contraseña sea correcta"
    echo "3. Tengas permisos de administrador"
    exit 1
fi

echo "Conexión exitosa. Creando base de datos y tablas..."
echo

# Ejecutar script SQL
mysql -u root -p$mysql_password < "scripde labd.sql"

if [ $? -eq 0 ]; then
    echo
    echo "========================================"
    echo "   BASE DE DATOS CONFIGURADA EXITOSAMENTE"
    echo "========================================"
    echo
    echo "Credenciales de administrador:"
    echo "- Usuario: admin1, admin2, admin3"
    echo "- Contraseña: admin123"
    echo
    echo "Usuarios de prueba:"
    echo "- Docentes: prof.martinez, prof.garcia, prof.lopez"
    echo "- Alumnos: alumno001, alumno002, alumno003, alumno004"
    echo "- Contraseña: password123"
    echo
    echo "La aplicación Spring Boot debería conectarse automáticamente."
    echo
else
    echo
    echo "ERROR: No se pudo configurar la base de datos"
    echo "Verifica que:"
    echo "1. MySQL esté ejecutándose"
    echo "2. La contraseña sea correcta"
    echo "3. Tengas permisos de administrador"
    echo
    exit 1
fi
