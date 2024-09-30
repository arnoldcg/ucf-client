# ucf
Practical exersice for "AC"

# Default values
1. **protocol** -- http
2. **host** -- localhost
3. **port** -- 8888

# How to run with Docker and DockerCompose
1. Install docker
2. Install gradle
3. Execute command in the root of the project
```cmd
gradle clean build 
docker-compose up
```

# Swagger url
1. URL format -- {{protocol}}://{{host}}:{{port}}/dev-ms-ucf
2. Example url for dev environment  -- http://localhost:8888/dev-ms-ucf/swagger-ui/index.html

# Extra features
1. Docker integration of the microservice
2. Docker compose created for fast testing
3. Using liquibase for database versioning
4. Using MapStruct and Lombook
5. Using Spring Boot Envers for tracking operations over the entities
6. Using swagger for endpoint documentation and OPEN API
7. Postman collection added to the main root of the project