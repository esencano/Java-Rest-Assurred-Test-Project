#!/usr/bin/env bash

echo "remove old reports"
#remove old reports
rm -rf test-output

#docker remove old container
#docker stop container-esencano-api-automation || true && docker rm container-esencano-api-automation|| true
docker rm -f container-esencano-api-automation 2> /dev/null || true

echo "build container"
#build docker image
docker build -f build/Dockerfile -t esencano/api-automation .

echo "run container"
#run docker container
docker run \
       --name container-esencano-api-automation \
       -e TEST_NAME=smoke \
       -e TEST_DB_URL=$TEST_DB_URL \
       -e TEST_DB_USERNAME=$TEST_DB_USERNAME \
       -e TEST_DB_PASSWORD=$TEST_DB_PASSWORD \
       esencano/api-automation
       #--rm
       #-v ${PWD}/test-output:/app/test-output \
       #-v ${PWD}/logs:/app/logs \
       #above lines can be used instead of docker cp, if this aprroach will be used then --rm flag can be used instead of docker rm line below.      

docker cp container-esencano-api-automation:/app/test-output test-output
docker cp container-esencano-api-automation:/app/logs logs

#remove docker container
docker rm -f container-esencano-api-automation 2> /dev/null || true
#remove docker image
docker image rm -f esencano/api-automation 2> /dev/null || true