package uk.co.mulecode.fileservice.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;
import uk.co.mulecode.fileservice.test.matchers.impl.HttpRequestEntityMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.mulecode.fileservice.test.matchers.JsonSchemaValidatorResultMatcher.validateSchema;

@ActiveProfiles("test-csv-validation-disabled")
class FileProcessorControllerValidationDisabledTest extends IntegrationTestBase {

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private HttpRequestEntityMatcher httpRequestEntityMatcher;

    @Test
    void processFile_InvalidCSVDisabledValidation_ReturnInternalServerError() throws Exception {

        String fileName = "data/entry_invalid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file).with(remoteAddr("200.0.0.3")))
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.message", Matchers.startsWith("Unhandled error exception - Error parsing CSV line")))
                .andExpect(jsonPath("$.error").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.exception").value("java.lang.RuntimeException"))
                .andExpect(jsonPath("$.path").value("/process/upload"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(validateSchema("./data/schema/ErrorV1Response.json"));

        httpRequestEntityMatcher.assertExistsByRequestIpAddress("200.0.0.3");
    }
}
