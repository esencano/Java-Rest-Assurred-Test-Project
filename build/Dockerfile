#FROM maven:3.8.6-eclipse-temurin-18
FROM maven:3.8.6-jdk-11

WORKDIR /app

COPY ../ .

ENV TEST_NAME="smoke"
ENV TEST_DB_URL=ec2-52-214-79-222.eu-west-1.compute.amazonaws.com/df3ndlrlgema8n
ENV TEST_DB_USERNAME=db_username
ENV TEST_DB_PASSWORD=db_password

CMD ["sh","-c","mvn clean test -Dsurefire.suiteXmlFiles=${TEST_NAME}.xml"]

#docker build -f build/Dockerfile -t test2 .

#docker run --rm \
#       -e TEST_NAME=smoke \
#       -e TEST_DB_URL=$TEST_DB_URL \
#       -e TEST_DB_USERNAME=$TEST_DB_USERNAME \
#       -e TEST_DB_PASSWORD=$TEST_DB_PASSWORD \
#       -v /Users/esencano/Desktop/reports:/app/test-output \
#       -v /Users/esencano/Desktop/reports:/app/logs \
#       test2

