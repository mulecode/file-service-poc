package uk.co.mulecode.fileservice.service;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

class FileIngestionServiceTest extends IntegrationTestBase {

    @Autowired
    private FileIngestionService fileIngestionService;

    @Test
    void processFile_ValidFile_ReturnProcessedJson() throws Exception {

        String fileName = "data/entry_valid.txt";
        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        String json = fileIngestionService.processFile(file);

        String expectedJson = readFileAsString("data/expectations/entry_valid_mapped.json");

        JSONAssert.assertEquals(expectedJson, json, JSONCompareMode.LENIENT);
    }
}
