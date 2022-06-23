# user-management

## Configure Database

### Docker Run

docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres

### Create Database and Execute DDL

Connect to database postgres with user postgres:

	hostaname: localhost
	port: 5432
	database: postgres
	user: postgres
	password: mysecretpassword

and run script:

    - CREATE_DATABASE_V1.sql

Connect to db to create table:

	hostaname: localhost
	port: 5432
	database: postgres
	user: user-mgmt-user
	password: user-mgmt-user
	
and run script:

		- CREATE_TABLE_V1.sql
    
## Deploy Application to Docker

To install app to docker you can use the script: ***deploy/install.sh*** or execute the following commands from code directory:

1. mvn clean package -Dmaven.test.skip
2. docker build --tag=user-management:1.0.0.0 .
3. docker stop user-management
4. docker rm user-management
5. docker run --name user-management -d -p 8080:8080 user-management:1.0.0.0

## Input File (CSV)

In /filInput folder there is a sample CSV

## Collection Postman

In /testPostman folder there is a collection Postman with test cases
