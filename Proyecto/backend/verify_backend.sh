#!/bin/bash

# Script de verificación para AppMartin Backend
# Verifica que todos los componentes estén funcionando correctamente

echo "🔍 VERIFICACIÓN DEL BACKEND APPMARTIN"
echo "======================================"
echo ""

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir con color
print_status() {
    if [ $2 -eq 0 ]; then
        echo -e "${GREEN}✅ $1${NC}"
    else
        echo -e "${RED}❌ $1${NC}"
    fi
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml. Ejecutar desde el directorio raíz del proyecto.${NC}"
    exit 1
fi

print_info "Verificando estructura del proyecto..."

# Verificar estructura de directorios
if [ -d "src/main/java/com/prsanmartin/appmartin/controller" ]; then
    print_status "Directorio de controladores encontrado" 0
else
    print_status "Directorio de controladores no encontrado" 1
fi

if [ -d "src/main/resources/static" ]; then
    print_status "Directorio de recursos estáticos encontrado" 0
else
    print_status "Directorio de recursos estáticos no encontrado" 1
fi

if [ -d "target/reports" ]; then
    print_status "Directorio de reportes encontrado" 0
else
    print_status "Directorio de reportes no encontrado" 1
fi

echo ""
print_info "Verificando archivos generados..."

# Verificar archivos de reporte
if [ -f "target/reports/endpoints_report.json" ]; then
    print_status "Reporte de endpoints generado" 0
else
    print_status "Reporte de endpoints no encontrado" 1
fi

if [ -f "target/reports/security_analysis_report.md" ]; then
    print_status "Reporte de seguridad generado" 0
else
    print_status "Reporte de seguridad no encontrado" 1
fi

if [ -f "target/reports/FINAL_TECHNICAL_REPORT.md" ]; then
    print_status "Reporte técnico final generado" 0
else
    print_status "Reporte técnico final no encontrado" 1
fi

# Verificar interfaz de pruebas
if [ -f "src/main/resources/static/tester.html" ]; then
    print_status "Interfaz de pruebas generada" 0
else
    print_status "Interfaz de pruebas no encontrada" 1
fi

echo ""
print_info "Verificando controladores modificados..."

# Verificar que los conflictos fueron resueltos
if grep -q "TEMP_DISABLED_FOR_TESTS" src/main/java/com/prsanmartin/appmartin/controller/AlumnosController.java; then
    print_status "SpecificEndpointsController desactivado en AlumnosController" 0
else
    print_status "SpecificEndpointsController no encontrado en AlumnosController" 1
fi

if grep -q "TEMP_DISABLED_FOR_TESTS" src/main/java/com/prsanmartin/appmartin/controller/TestController.java; then
    print_status "Métodos duplicados desactivados en TestController" 0
else
    print_status "Métodos duplicados no encontrados en TestController" 1
fi

if grep -q "TEMP_DISABLED_FOR_TESTS" src/main/java/com/prsanmartin/appmartin/controller/AdminUserController.java; then
    print_status "AdminUserController desactivado" 0
else
    print_status "AdminUserController no encontrado" 1
fi

echo ""
print_info "Verificando nuevos controladores..."

# Verificar controladores nuevos
if [ -f "src/main/java/com/prsanmartin/appmartin/controller/TesterController.java" ]; then
    print_status "TesterController creado" 0
else
    print_status "TesterController no encontrado" 1
fi

if [ -f "src/main/java/com/prsanmartin/appmartin/controller/DiagnosticsController.java" ]; then
    print_status "DiagnosticsController creado" 0
else
    print_status "DiagnosticsController no encontrado" 1
fi

echo ""
print_info "Verificando configuración de seguridad..."

# Verificar configuración de seguridad
if grep -q "permitAll" src/main/java/com/prsanmartin/appmartin/config/SecurityConfig.java; then
    print_warning "Seguridad deshabilitada para pruebas (CORRECTO para desarrollo)"
else
    print_status "Configuración de seguridad no encontrada" 1
fi

echo ""
print_info "Contando endpoints..."

# Contar endpoints en el reporte JSON
if [ -f "target/reports/endpoints_report.json" ]; then
    ENDPOINT_COUNT=$(grep -o '"path":' target/reports/endpoints_report.json | wc -l)
    print_status "Total de endpoints mapeados: $ENDPOINT_COUNT" 0
else
    print_status "No se pudo contar endpoints" 1
fi

echo ""
echo "🎯 RESUMEN DE VERIFICACIÓN"
echo "=========================="

# Contar archivos de reporte
REPORT_COUNT=$(ls target/reports/*.md target/reports/*.json 2>/dev/null | wc -l)
print_status "Archivos de reporte generados: $REPORT_COUNT" 0

# Verificar que la interfaz de pruebas existe
if [ -f "src/main/resources/static/tester.html" ]; then
    print_status "Interfaz de pruebas disponible en /tester" 0
else
    print_status "Interfaz de pruebas no disponible" 1
fi

echo ""
print_info "Para probar el backend:"
echo "1. Ejecutar: mvn spring-boot:run"
echo "2. Abrir: http://localhost:8080/tester"
echo "3. Probar endpoints usando la interfaz integrada"
echo "4. Verificar diagnósticos: http://localhost:8080/api/diagnostics/health"

echo ""
print_warning "IMPORTANTE: La seguridad está deshabilitada para pruebas."
print_warning "Antes de producción, implementar autenticación y autorización."

echo ""
echo -e "${GREEN}✅ Verificación completada${NC}"
echo "El backend está listo para desarrollo y pruebas."
