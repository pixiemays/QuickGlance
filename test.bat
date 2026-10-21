@echo off
setlocal
cd /d "%~dp0"

if not "%1"=="" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" %*
    goto end
)

:menu
cls
echo ========================================================
echo               QuickGlance Helper Tool
echo ========================================================
echo  [1] Run automated tests
echo  [2] Build all 5 JAR files into build/libs
echo  [3] Launch Client: Minecraft 26.3 (Fabric)
echo  [4] Launch Client: Minecraft 26.3 (NeoForge)
echo  [5] Launch Client: Minecraft 1.21.1 (Fabric)
echo  [6] Launch Client: Minecraft 1.21.1 (NeoForge)
echo  [7] Launch Client: Minecraft 1.20.1 (Fabric)
echo  [8] Clean project cache
echo  [0] Exit
echo ========================================================
set /p choice="Select an option (0-8): "

if "%choice%"=="1" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -Test
    echo.
    pause
    goto menu
)
if "%choice%"=="2" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -BuildAll
    echo.
    pause
    goto menu
)
if "%choice%"=="3" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -RunClient -Version 26.3 -Loader fabric
    echo.
    pause
    goto menu
)
if "%choice%"=="4" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -RunClient -Version 26.3 -Loader neoforge
    echo.
    pause
    goto menu
)
if "%choice%"=="5" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -RunClient -Version 1.21.1 -Loader fabric
    echo.
    pause
    goto menu
)
if "%choice%"=="6" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -RunClient -Version 1.21.1 -Loader neoforge
    echo.
    pause
    goto menu
)
if "%choice%"=="7" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -RunClient -Version 1.20.1 -Loader fabric
    echo.
    pause
    goto menu
)
if "%choice%"=="8" (
    powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0test-mod.ps1" -Clean
    echo.
    pause
    goto menu
)
if "%choice%"=="0" goto end

:end
