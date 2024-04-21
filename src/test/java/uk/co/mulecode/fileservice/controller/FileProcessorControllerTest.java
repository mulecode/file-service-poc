package uk.co.mulecode.fileservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;
import uk.co.mulecode.fileservice.test.matchers.impl.HttpRequestEntityMatcher;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.mulecode.fileservice.test.matchers.JsonSchemaValidatorResultMatcher.validateSchema;

class FileProcessorControllerTest extends IntegrationTestBase {

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private HttpRequestEntityMatcher httpRequestEntityMatcher;

    @Test
    void processFile_ValidFile_ReturnOk() throws Exception {

        String fileName = "data/entry_valid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file).file(file).with(remoteAddr("200.0.0.0")))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/octet-stream"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"OutcomeFile.json\""))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(validateSchema("./data/schema/OutcomeV1Response.json"));

        httpRequestEntityMatcher.assertExistsByRequestIpAddress("200.0.0.0");
    }

    @Test
    void processFile_InvalidEmpty_ReturnBadRequest() throws Exception {

        String fileName = "data/entry_invalid_empty.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file).with(remoteAddr("200.0.0.1")))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message").value("Validation failed, error count: 1"))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.exception").value("jakarta.validation.ConstraintViolationException"))
                .andExpect(jsonPath("$.path").value("/process/upload"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(validateSchema("./data/schema/ErrorV1Response.json"));

        httpRequestEntityMatcher.assertExistsByRequestIpAddress("200.0.0.1");
    }

    @Test
    void processFile_InvalidIpOrigin_ReturnUnauthorized() throws Exception {

        String fileName = "data/entry_valid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file).with(remoteAddr("403.0.0.0")))
                .andExpect(status().isForbidden())
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message").value("GeoLocation Policy Rule - Request was forbidden"))
                .andExpect(jsonPath("$.error").value("FORBIDDEN"))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.exception").value("uk.co.mulecode.policy.GeoLocationPolicyException"))
                .andExpect(jsonPath("$.path").value("/process/upload"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(validateSchema("./data/schema/ErrorV1Response.json"));

        httpRequestEntityMatcher.assertExistsByRequestIpAddress("403.0.0.0");
    }
}
