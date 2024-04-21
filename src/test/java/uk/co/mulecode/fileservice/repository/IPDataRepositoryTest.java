package uk.co.mulecode.fileservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.co.mulecode.fileservice.test.matchers.impl.JsonSchemaValidatorMatcher.assertSchema;

public class IPDataRepositoryTest extends IntegrationTestBase {

    @Autowired
    private IPDataRepository ipDataRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void getIPdata_apiConnection_ReturnData() {

        String ipAddress = "200.0.0.0";

        final IPDataResponse iPdata = ipDataRepository.getIPdata(ipAddress);

        assertNotNull(iPdata);
        assertSchema(jsonMapper.toJson(iPdata), "./data/schema/IPDataV1Response.json");
    }

    @Test
    void getIPdata_apiConnectionError_ReturnError() {

        String ipAddress = "500.0.0.0";

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> {
            ipDataRepository.getIPdata(ipAddress);
        });

        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getMessage().contains("Internal server error"));
        assertNotNull(exception.getResponseBodyAsString());
        assertSchema(exception.getResponseBodyAsString(), "./data/schema/IPDataV1500Response.json");
    }
}
