#!/bin/bash

git pull
mvn -q -DskipTests package
java -jar target/Goblins-jar-with-dependencies.jar
