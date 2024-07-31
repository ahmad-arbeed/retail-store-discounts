# retail-store-discounts

RETAIL STORE DISCOUNTS PROJECT CONSIST OF THREE MAIN SPRING BOOT SERVICES AND A GW

------------


**Prerequisite**
1. maven
2. jdk 21
3. docker daemon

------------


###### Services:
* gw: a spring cloud gateway service used to route any request to the target service, and also has to be authenticated and authorized request
	- It has two temp users {admin/admin} {user/user}
	- admin has access to POST/PUT/GET/DELETE
	- user has access to GET and POST discount
* users: a service used to handle users crud opertaion 
* products: a service used to handle products crud opertaion
* discounts: main service where net payable amount get calculated
* store-common:
Is a dependency jar that has common functions (exceptions, exception-handler, api response object)

------------


###### Run Application:

You can run the services manually one by one using IDE or from the command line, you have to setup mognodb uri, and ports

`mvn spring-boot:run`

Also you can use docker-compose file within the project to run all services and mognodb
1. run build-all.sh
2. you will get message if you want to build docker image enter `yes`
3. once completed all docker images will be ready to use 
	- users:latest
	- products:latest
	- discounts:latest
4. run docker compose to start the services
`docker-compose up -d`

5. you can start using the services

------------

###### Run Test Cases and Jacoco Report
You can run test cases and jacoco report by the following:
1. Manually by locating and open SERVICE_NAME/target/site/jacoco/index.html
2. Or you can run build.sh inside each service folder then it will run maven test and then open the jacoco report automatically.
`sh build.sh`


------------
###### Swagger
* products: http://localhost:8081/products/swagger-ui/index.html
* users: http://localhost:8082/users/swagger-ui/index.html
* discounts: http://localhost:8083/discounts/swagger-ui/index.html

------------
###### SonarQube
You will find SonarQube screenshots for each service in ***reports*** folder.
