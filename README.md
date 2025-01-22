# RickAndMortyAPI
This project is designed to validate various endpoints of the Rick and Morty API using Rest-Assured and Java. It includes test cases for multiple API endpoints such as characters, locations, and episodes, ensuring the integrity and accuracy of the API responses.

## Features

Automated testing of Rick and Morty API endpoints.
Utilizes Rest-Assured for API testing.
Built with Java and integrates with Maven for dependency management and build processes.
Includes parallel and sequential test execution support using TestNG.
Detailed logging and reporting for test results.

## Prerequisites
Before running the tests, ensure you have the following installed:

Java 11 or later.
Apache Maven.
A compatible IDE  IntelliJ IDEA or Eclipse

# Installation

Clone the repository

`mvn clean install`

`mvn test`

## Test Execution Modes

### Parallel Execution

Tests can be executed in parallel for faster results. This is configured in the **RegressionBackEndParallel.xml** file.

`mvn test -DsuiteXmlFile=RegressionBackEndParallel`

### Sequential Execution
For sequential test execution, the RegressionBackEndSequential.xml file is used.

`mvn test -DsuiteXmlFile=testng-sequential.xml`