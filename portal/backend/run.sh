#!/bin/bash

# Job Portal Backend - Run Script

echo "Starting Job Portal Backend..."
echo "================================"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed!"
    echo "Please install Maven from: https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed!"
    echo "Please install Java JDK 11+ from: https://www.oracle.com/java/technologies/"
    exit 1
fi

echo "✓ Maven found: $(mvn -version | head -n 1)"
echo "✓ Java found: $(java -version 2>&1 | head -n 1)"

# Build and run
echo ""
echo "Building project..."
mvn clean install

if [ $? -eq 0 ]; then
    echo ""
    echo "================================"
    echo "Build successful! Starting server..."
    echo "Backend will run on: http://localhost:8080"
    echo "API endpoints available at: http://localhost:8080/api"
    echo "================================"
    echo ""
    mvn spring-boot:run
else
    echo "Build failed! Check errors above."
    exit 1
fi
