@echo off
REM BallWars Build Script for Windows

echo Building BallWars...

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed. Continuing without Maven...
)

REM Run Maven build if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    call mvn clean package
    
    if %ERRORLEVEL% EQU 0 (
        echo Build completed successfully!
    ) else (
        echo Build failed!
        exit /b 1
    )
) else (
    echo Skipping Maven build. Please install Maven and run this script again.
    exit /b 1
)
