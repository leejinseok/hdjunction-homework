#!/bin/sh

./gradlew core:clean
./gradlew api:clean
./gradlew api:build

java -jar api/build/libs/api-0.0.1-SNAPSHOT.jar