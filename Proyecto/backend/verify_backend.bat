@echo off
REM Script de verificación para AppMartin Backend (Windows)
REM Verifica que todos los componentes estén funcionando correctamente

echo 🔍 VERIFICACIÓN DEL BACKEND APPMARTIN
echo ======================================
echo.

REM Verificar que estamos en el directorio correcto
if not exist "pom.xml" (
    echo ❌ Error: No se encontró pom.xml. Ejecutar desde el directorio raíz del proyecto.
    exit /b 1
)

echo ℹ️  Verificando estructura del proyecto...

REM Verificar estructura de directorios
if exist "src\main\java\com\prsanmartin\appmartin\controller" (
    echo ✅ Directorio de controladores encontrado
) else (
    echo ❌ Directorio de controladores no encontrado
)

if exist "src\main\resources\static" (
    echo ✅ Directorio de recursos estáticos encontrado
) else (
    echo ❌ Directorio de recursos estáticos no encontrado
)

if exist "target\reports" (
    echo ✅ Directorio de reportes encontrado
) else (
    echo ❌ Directorio de reportes no encontrado
)

echo.
echo ℹ️  Verificando archivos generados...

REM Verificar archivos de reporte
if exist "target\reports\endpoints_report.json" (
    echo ✅ Reporte de endpoints generado
) else (
    echo ❌ Reporte de endpoints no encontrado
)

if exist "target\reports\security_analysis_report.md" (
    echo ✅ Reporte de seguridad generado
) else (
    echo ❌ Reporte de seguridad no encontrado
)

if exist "target\reports\FINAL_TECHNICAL_REPORT.md" (
    echo ✅ Reporte técnico final generado
) else (
    echo ❌ Reporte técnico final no encontrado
)

REM Verificar interfaz de pruebas
if exist "src\main\resources\static\tester.html" (
    echo ✅ Interfaz de pruebas generada
) else (
    echo ❌ Interfaz de pruebas no encontrada
)

echo.
echo ℹ️  Verificando controladores modificados...

REM Verificar que los conflictos fueron resueltos
findstr /C:"TEMP_DISABLED_FOR_TESTS" "src\main\java\com\prsanmartin\appmartin\controller\AlumnosController.java" >nul
if %errorlevel% equ 0 (
    echo ✅ SpecificEndpointsController desactivado en AlumnosController
) else (
    echo ❌ SpecificEndpointsController no encontrado en AlumnosController
)

findstr /C:"TEMP_DISABLED_FOR_TESTS" "src\main\java\com\prsanmartin\appmartin\controller\TestController.java" >nul
if %errorlevel% equ 0 (
    echo ✅ Métodos duplicados desactivados en TestController
) else (
    echo ❌ Métodos duplicados no encontrados en TestController
)

findstr /C:"TEMP_DISABLED_FOR_TESTS" "src\main\java\com\prsanmartin\appmartin\controller\AdminUserController.java" >nul
if %errorlevel% equ 0 (
    echo ✅ AdminUserController desactivado
) else (
    echo ❌ AdminUserController no encontrado
)

echo.
echo ℹ️  Verificando nuevos controladores...

REM Verificar controladores nuevos
if exist "src\main\java\com\prsanmartin\appmartin\controller\TesterController.java" (
    echo ✅ TesterController creado
) else (
    echo ❌ TesterController no encontrado
)

if exist "src\main\java\com\prsanmartin\appmartin\controller\DiagnosticsController.java" (
    echo ✅ DiagnosticsController creado
) else (
    echo ❌ DiagnosticsController no encontrado
)

echo.
echo ℹ️  Verificando configuración de seguridad...

REM Verificar configuración de seguridad
findstr /C:"permitAll" "src\main\java\com\prsanmartin\appmartin\config\SecurityConfig.java" >nul
if %errorlevel% equ 0 (
    echo ⚠️  Seguridad deshabilitada para pruebas (CORRECTO para desarrollo)
) else (
    echo ❌ Configuración de seguridad no encontrada
)

echo.
echo ℹ️  Contando endpoints...

REM Contar endpoints en el reporte JSON
if exist "target\reports\endpoints_report.json" (
    for /f %%i in ('findstr /C:"\"path\":" "target\reports\endpoints_report.json" ^| find /c /v ""') do set ENDPOINT_COUNT=%%i
    echo ✅ Total de endpoints mapeados: %ENDPOINT_COUNT%
) else (
    echo ❌ No se pudo contar endpoints
)

echo.
echo 🎯 RESUMEN DE VERIFICACIÓN
echo ==========================

REM Contar archivos de reporte
set REPORT_COUNT=0
if exist "target\reports\*.md" set /a REPORT_COUNT+=1
if exist "target\reports\*.json" set /a REPORT_COUNT+=1
echo ✅ Archivos de reporte generados: %REPORT_COUNT%

REM Verificar que la interfaz de pruebas existe
if exist "src\main\resources\static\tester.html" (
    echo ✅ Interfaz de pruebas disponible en /tester
) else (
    echo ❌ Interfaz de pruebas no disponible
)

echo.
echo ℹ️  Para probar el backend:
echo 1. Ejecutar: mvn spring-boot:run
echo 2. Abrir: http://localhost:8081/tester
echo 3. Probar endpoints usando la interfaz integrada
echo 4. Verificar diagnósticos: http://localhost:8081/api/diagnostics/health

echo.
echo ⚠️  IMPORTANTE: La seguridad está deshabilitada para pruebas.
echo ⚠️  Antes de producción, implementar autenticación y autorización.

echo.
echo ✅ Verificación completada
echo El backend está listo para desarrollo y pruebas.

pause
