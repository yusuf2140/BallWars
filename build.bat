@echo off
REM BallWars Build Script for Windows

echo Building BallWars...

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed. Please install Maven to build this project.
    exit /b 1
)

REM Run Maven build
call mvn clean package

if %ERRORLEVEL% EQU 0 (
    echo Build completed successfully!
) else (
    echo Build failed!
    exit /b 1
)
