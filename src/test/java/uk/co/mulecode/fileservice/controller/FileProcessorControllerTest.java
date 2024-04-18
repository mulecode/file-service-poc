package uk.co.mulecode.fileservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileProcessorControllerTest extends IntegrationTestBase {

    @Test
    void processFile_ValidFile_ReturnOk() throws Exception {

        String fileName = "data/entry_valid.txt";

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        getMvc().perform(multipart("/process/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/octet-stream"))
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"OutcomeFile.json\""))
                .andExpect(content().string(new String(file.getBytes())));
    }
}
