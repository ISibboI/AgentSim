#!/bin/bash

echo "$(tput setaf 2)Updating application...$(tput sgr 0)"
git pull
echo "$(tput setaf 2)Done.$(tput sgr 0)"
echo "$(tput setaf 2)Building application...$(tput sgr 0)"
mvn -q -DskipTests package
echo "$(tput setaf 2)Done.$(tput sgr 0)"
echo "$(tput setaf 2)Running application...$(tput sgr 0)"
java -jar target/Goblins-jar-with-dependencies.jar
echo "$(tput setaf 2)JVM terminated.$(tput sgr 0)"
