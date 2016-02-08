#!/bin/bash

echo "Updating application"
git pull
echo "Building application"
mvn -q -DskipTests package
echo "Running application"
java -jar target/Goblins-jar-with-dependencies.jar
echo "JVM terminated"
