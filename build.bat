@echo off
REM BallWars Build Script for Windows

echo Building BallWars...
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Maven found. Building with Maven...
    call mvn clean package
    
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo Build completed successfully with Maven!
    ) else (
        echo.
        echo Maven build failed!
        pause
        exit /b 1
    )
) else (
    echo Maven is not installed. Building JAR manually...
    echo.
    
    REM Create build directories
    if not exist "build\classes" mkdir build\classes
    if not exist "build\jar" mkdir build\jar
    
    REM Compile Java files
    echo Compiling Java source files...
    for /r "src" %%f in (*.java) do (
        javac -d build\classes "%%f"
    )
    
    if %ERRORLEVEL% NEQ 0 (
        echo.
        echo Compilation failed!
        pause
        exit /b 1
    )
    
    REM Create JAR file
    echo Creating JAR file...
    cd build\classes
    jar cvfe ..\jar\BallWars.jar Main *
    cd ..\..
    
    if %ERRORLEVEL% EQU 0 (
        echo.
        echo Build completed successfully! JAR created at build\jar\BallWars.jar
    ) else (
        echo.
        echo JAR creation failed!
        pause
        exit /b 1
    )
)

echo.
echo Press any key to close this window...
pause >nul
