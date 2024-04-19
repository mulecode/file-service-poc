package uk.co.mulecode.fileservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;
import uk.co.mulecode.stubs.IPApiStub;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.co.mulecode.fileservice.utils.matchers.impl.JsonSchemaValidatorMatcher.assertSchema;
import static uk.co.mulecode.stubs.IPApiStub.TEST_IP;

public class IPDataRepositoryTest extends IntegrationTestBase {

    @Autowired
    private IPDataRepository ipDataRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void getIPdata_apiConnection_ReturnData() {

        final IPDataResponse ipDataResponse = givenOneObjectOf(IPDataResponse.class);

        IPApiStub.stubSuccessApiResponse(jsonMapper.toJson(ipDataResponse));

        final IPDataResponse iPdata = ipDataRepository.getIPdata(TEST_IP);

        assertNotNull(iPdata);
        assertSchema(jsonMapper.toJson(iPdata), "./data/schema/IPDataV1Response.json");
    }

    @Test
    void getIPdata_apiConnectionError_ReturnError() {

        IPApiStub.stubInternalServerApiResponse(jsonMapper.toJson(Map.of("error", "Internal server error")));

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> {
            ipDataRepository.getIPdata(TEST_IP);
        });

        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getMessage().contains("Internal server error"));
        assertNotNull(exception.getResponseBodyAsString());
        assertSchema(exception.getResponseBodyAsString(), "./data/schema/IPDataV1500Response.json");
    }
}
