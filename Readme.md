#java, #rest-assured, #testng, #maven, #lombok, #allure, #log4j2, #jackson, #gson, #docker, #jenkins

### Description
This project has been created just to write some simple api automation. In the project in some places used different approches,   different methods to show usage that's why consistency between tests should not been expected. Some test cases can be changed, some verification can beed added to some test cases also many other test cases can be added to this project.

### Project Structure:

 - Tests for each api created under specific package under
   src/test/java. Ex: user.api, home.api
 - Helper classes has been added to specific packages under
   src/main/java. Ex: filter, logger
 - Configuration folders can be found under src/main/resources
 - Reports can be found under test-output folder
 - Logs can be found under logs folder

### Setup
#### Run test locally
To run test locally, below environment variables should be set and then run command
```sh
 mvn clean test -Dsurefire.suiteXmlFiles=suiteFileName.xml
```
##### Environment variables
TEST_DB_URL
TEST_DB_USERNAME
TEST_DB_PASSWORD

#### Run test with docker
1- Build DockerFile
```sh
docker build -f build/Dockerfile -t api-automation-project .
```
2- Run container with environment variables and give volume path for output of test-reports and logs, also TEST_NAME environment variable should be set name of suite file without its extension

```sh
docker run --rm \
 -e TEST_NAME=smoke \
 -e TEST_DB_URL=$TEST_DB_URL \
 -e TEST_DB_USERNAME=$TEST_DB_USERNAME \
 -e TEST_DB_PASSWORD=$TEST_DB_PASSWORD \
 -v output_path/reports:/app/test-output \
 -v  output_path/logs:/app/logs \
 api-automation-project 
 ```	


#### Run with jenkins
On Jenkins same enviroment variables should be set or they can be set as credentials.
It will be enough to run below command in a jenkins job. In jenkins job TEST_NAME environment variable can be parameterized so it will support more flexibility

```sh
./build/docker_jenkins.bash 
```	


### Reports
Under test-ouput folder, there can be seen different kind of reports.
Allure should be installed locally to see allure reports on browser. below command should be run after installing allure.
```sh
allure serve test-output/allure-results 
```	

 - TestNG reports and html file
 - JUnit reports
 - Allure report
