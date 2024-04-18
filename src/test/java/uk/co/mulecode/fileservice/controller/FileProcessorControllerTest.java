package uk.co.mulecode.fileservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.mulecode.fileservice.utils.matchers.JsonSchemaResultMatcher.validateSchema;

class FileProcessorControllerTest extends IntegrationTestBase {

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
}
