package uk.co.mulecode.fileservice.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.component.properties.AppProperty;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.mulecode.fileservice.utils.matchers.JsonSchemaResultMatcher.validateSchema;

class FileProcessorControllerTest extends IntegrationTestBase {

    @MockBean
    private AppProperty appProperty;

    @BeforeEach
    void setUp() {
        //Default behaviour
        AppProperty.FeaturesFlags FeaturesFlagsMock = mock(AppProperty.FeaturesFlags.class);
        when(FeaturesFlagsMock.isEnableCsvFileValidation()).thenReturn(true);
        when(appProperty.getFeaturesFlags()).thenReturn(FeaturesFlagsMock);
    }

    @Test
    void processFile_ValidFile_ReturnOk() throws Exception {

        String fileName = "data/entry_valid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/octet-stream"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"OutcomeFile.json\""))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(validateSchema("./data/schema/OutcomeV1Response.json"));
    }

    @Test
    void processFile_InvalidEmpty_ReturnBadRequest() throws Exception {

        String fileName = "data/entry_invalid_empty.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file))
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
    }

    @Test
    void processFile_InvalidCSVDisabledValidation_ReturnInternalServerError() throws Exception {
        // Disable CSV file validation
        AppProperty.FeaturesFlags FeaturesFlagsMock = mock(AppProperty.FeaturesFlags.class);
        when(FeaturesFlagsMock.isEnableCsvFileValidation()).thenReturn(false);
        when(appProperty.getFeaturesFlags()).thenReturn(FeaturesFlagsMock);

        String fileName = "data/entry_invalid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file))
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
    }
}
