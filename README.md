# File Service (POC)

An example spring boot application to handle file uploads and processing.

The project has the intention of receive a file via rest api and returning a processed file back to the api consumer.

---

### Running the project

To run the project locally, ensure to install the project to it can generate a JAR file

```bash
make install
```

THe command above will install the project via docker and generate a JAR file at the root file of this project

```bash
make run
```

The project will run via docker in a secured java environment and will be available on port 9090

---

### Testing the project running via health endpoint

```bash
curl --location 'http://localhost:9090/app/actuator/health'
```
