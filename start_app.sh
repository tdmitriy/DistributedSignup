#!/bin/bash

set -x

# build all microservices & docker images
./gradlew clean buildAll

# stop compose if running
docker-compose -f compose/docker-compose.yml down

# run compose
docker-compose -f compose/docker-compose.yml up