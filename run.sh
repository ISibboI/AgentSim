#!/bin/bash

echo "Updating application..."
git pull
echo "Done."
echo "Building application..."
mvn -q -DskipTests package
echo "Done."
echo "Running application..."
java -jar target/Goblins-jar-with-dependencies.jar
echo "JVM terminated."
