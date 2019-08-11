# Distributed Signup
Test task - system allows to signup a player and store his data in persistence storage (REST + Kafka)

# How to run
1. Clone the project
2. Install docker-compose
3. Run `sh start_app.sh` to build all microservices, docker images and also it runs docker compose command
4. Wait for `docker-compose` build
5. Run [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) and test functionality
6. Run `sh stop_app.sh` to stop compose
