@echo off
REM BallWars Build Script for Windows

echo Building BallWars...

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed.
    echo.
    echo Would you like to:
    echo   Y - Continue without Maven
    echo   N - Open Maven download website
    echo.
    set /p choice="Enter your choice (Y/N): "
    
    REM Trim whitespace and convert to uppercase
    for /f "tokens=*" %%A in ('echo %choice%') do set choice=%%A
    
    if /i "%choice%"=="N" (
        echo Opening Maven website...
        start https://maven.apache.org/download.cgi
        exit /b 1
    ) else if /i "%choice%"=="Y" (
        echo Continuing without Maven...
        goto :continue
    ) else (
        echo Invalid choice. Please enter Y or N.
        exit /b 1
    )
)

:continue
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
