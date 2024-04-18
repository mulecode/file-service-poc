package uk.co.mulecode.fileservice.utils.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class JsonSchemaResultMatcher implements ResultMatcher {

    private final String schemaFilePath;

    public JsonSchemaResultMatcher(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public static JsonSchemaResultMatcher validateSchema(String schemaFilePath) {
        return new JsonSchemaResultMatcher(schemaFilePath);
    }

    @Override
    public void match(MvcResult result) throws Exception {
        var jsonString = result.getResponse().getContentAsString();
        log.info("Validating....: {}", jsonString);
        log.info("Validating....: {}", this.schemaFilePath);

        var dataSchemaFilePath = Paths.get(this.schemaFilePath);
        if (!Files.exists(dataSchemaFilePath)) {
            throw new AssertionError("Schema file not found: " + this.schemaFilePath);
        }
        var schemaString = Files.readString(dataSchemaFilePath);
        log.info("Validating....: {}", schemaString);

        JSONObject rawSchema = new JSONObject(new JSONTokener(schemaString));
        Schema schema = SchemaLoader.load(rawSchema);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonString);
        if (jsonNode.isArray()) {
            validate(schema, new JSONArray(jsonString));
        } else {
            validate(schema, new JSONObject(jsonString));
        }
    }

    private void validate(Schema schema, Object json) throws Exception {
        try {
            schema.validate(json);
        } catch (ValidationException e) {
            String violationDetails = String.join(",\nError at: ", e.getAllMessages());
            throw new Exception("Validation failed: \nError at: " + violationDetails, e);
        }
    }

}
