package uk.co.mulecode.fileservice.utils.matchers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import uk.co.mulecode.fileservice.utils.matchers.impl.JsonSchemaValidatorMatcher;

@Slf4j
public class JsonSchemaValidatorResultMatcher implements ResultMatcher {

    private final String schemaFilePath;

    public JsonSchemaValidatorResultMatcher(String schemaFilePath) {
        this.schemaFilePath = schemaFilePath;
    }

    public static JsonSchemaValidatorResultMatcher validateSchema(String schemaFilePath) {
        return new JsonSchemaValidatorResultMatcher(schemaFilePath);
    }

    @Override
    public void match(MvcResult result) throws Exception {
        var jsonString = result.getResponse().getContentAsString();
        JsonSchemaValidatorMatcher.assertSchema(jsonString, this.schemaFilePath);
    }

}
