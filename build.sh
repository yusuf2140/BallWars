#!/bin/bash

# BallWars Build Script for Unix/Linux/Mac

echo "Building BallWars..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed. Please install Maven to build this project."
    exit 1
fi

# Run Maven build
mvn clean package

if [ $? -eq 0 ]; then
    echo "Build completed successfully!"
else
    echo "Build failed!"
    exit 1
fi
