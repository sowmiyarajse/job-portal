@echo off
REM Job Portal Backend - Run Script for Windows

echo Starting Job Portal Backend...
echo ================================

REM Check if Maven is installed
mvn -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Maven is not installed!
    echo Please install Maven from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed!
    echo Please install Java JDK 11+ from: https://www.oracle.com/java/technologies/
    pause
    exit /b 1
)

echo Build and run commands:
echo mvn clean install
echo mvn spring-boot:run
echo.
echo Or simply run the Spring Boot application!
echo.
echo Executing: mvn spring-boot:run
echo.

mvn spring-boot:run

if errorlevel 1 (
    echo Build or execution failed!
    pause
    exit /b 1
)
