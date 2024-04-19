package uk.co.mulecode.fileservice.utils.matchers.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class JsonSchemaValidatorMatcher {

    public static void assertSchema(String jsonString, String schemaFilePath) throws AssertionError {
        try {
            log.debug("Validating schema {} against json {}", schemaFilePath, jsonString);
            var dataSchemaFilePath = Paths.get(schemaFilePath);
            var schemaString = Files.readString(dataSchemaFilePath);
            log.debug("Schema loaded: {}", schemaString);

            JSONObject rawSchema = new JSONObject(new JSONTokener(schemaString));
            Schema schema = SchemaLoader.load(rawSchema);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonString);
            if (jsonNode.isArray()) {
                schema.validate(new JSONArray(jsonString));
            } else {
                schema.validate(new JSONObject(jsonString));
            }
            log.debug("Success schema {} is valid for json {}", schemaFilePath, jsonString);
        } catch (ValidationException e) {
            String violationDetails = String.join(",\nError at: ", e.getAllMessages());
            throw new AssertionError("Validation failed: \nError at: " + violationDetails, e);
        } catch (IOException e) {
            throw new AssertionError("Error reading schema file: " + schemaFilePath, e);
        }
    }
}
