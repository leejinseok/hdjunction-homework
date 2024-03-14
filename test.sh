#!/bin/sh

./gradlew api:clean
./gradlew api:test

./gradlew core:clean
./gradlew core:test