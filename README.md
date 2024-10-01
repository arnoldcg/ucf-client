# ucf
Practical exersice for "AC"

# Default values
1. **protocol** -- http
2. **host** -- localhost
3. **port** -- 8889

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
2. Example url for dev environment  -- http://localhost:8889/dev-ms-ucf-client/swagger-ui/index.html

**In the Postman Collection the authentication for the client is disabled so the code is adding the headers dynamically**