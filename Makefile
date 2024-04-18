COMPOSE_RUN_JAVA = docker-compose run --no-deps --rm java
COMPOSE_RUN_JAVA_RUN = docker-compose run --no-deps --rm java_run
COMPOSE_RUN_MVN = docker-compose run --no-deps --entrypoint "mvn" --rm java
COMPOSE_RUN_JAVA_MAKE = docker-compose run --no-deps --entrypoint "make" --rm java

.PHONY: version
version: ## Java version with docker run pattern
	$(COMPOSE_RUN_JAVA) --version

.PHONY: clean
clean: ## Maven Clean
	$(COMPOSE_RUN_MVN) clean

.PHONY: test
test: ## Maven Test
	$(COMPOSE_RUN_MVN) test

.PHONY: install
install: clean
install: ## Maven Install
	$(COMPOSE_RUN_MVN) install

.PHONY: run_docker
run_docker: PORT = 9090
run_docker: JAR_FILE = ./target/file-service-poc-0.0.1-SNAPSHOT.jar
run_docker: ## Run
	@docker run -it -v .:/opt/app -p $(PORT):8080 --entrypoint "java" file-service-poc:latest -jar $(JAR_FILE)


.PHONY: run
run: PORT = 9090
run: JAR_FILE = ./target/file-service-poc-0.0.1-SNAPSHOT.jar
run: ## Run
	@docker-compose run -p $(PORT):8080 java -jar $(JAR_FILE)


