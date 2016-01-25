#!/bin/bash

git pull
mvn -DskipTests package
java -jar target/Goblins-jar-with-dependencies.jar
