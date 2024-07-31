#!/bin/bash

build_docker_image() {
    local service_name=$1
    echo "Do you want to build the Docker image for ${service_name}? (yes/no)"
    read -r build_image
    if [ "$build_image" == "yes" ]; then
        docker build -t "${service_name}:latest" .
        echo "${service_name} Docker image built successfully."
    else
        echo "Skipping Docker build for ${service_name}."
    fi
}

echo "Building store-common..."
cd ./store-common || exit
mvn clean install

echo "Building users..."
cd ../users || exit
mvn clean install
build_docker_image "users"

echo "Building products..."
cd ../products || exit
mvn clean install
build_docker_image "products"

echo "Building discounts..."
cd ../discounts || exit
mvn clean install
build_docker_image "discounts"

echo "All projects built successfully."
echo "Please locate to SERVICE_NAME/target/site to find jacoco report coverage"