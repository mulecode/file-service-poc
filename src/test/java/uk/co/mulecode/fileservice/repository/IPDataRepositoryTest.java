package uk.co.mulecode.fileservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpServerErrorException;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.co.mulecode.fileservice.utils.matchers.impl.JsonSchemaValidatorMatcher.assertSchema;

class IPDataRepositoryTest extends IntegrationTestBase {

    public static final String TEST_IP = "24.48.0.1";

    @Autowired
    private IPDataRepository ipDataRepository;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void getIPdata_apiConnection_ReturnData() {

        final IPDataResponse ipDataResponse = givenOneObjectOf(IPDataResponse.class);

        stubFor(get(urlEqualTo("/json/" + TEST_IP + "?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonMapper.toJson(ipDataResponse))));

        final IPDataResponse iPdata = ipDataRepository.getIPdata(TEST_IP);

        assertNotNull(iPdata);
        assertSchema(jsonMapper.toJson(iPdata), "./data/schema/IPDataV1Response.json");

    }

    @Test
    void getIPdata_apiConnectionError_ReturnError() {

        stubFor(get(urlEqualTo("/json/" + TEST_IP + "?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonMapper.toJson(Map.of("error", "Internal server error")))));

        HttpServerErrorException exception = assertThrows(HttpServerErrorException.class, () -> {
            ipDataRepository.getIPdata(TEST_IP);
        });

        assertEquals(500, exception.getStatusCode().value());
        assertTrue(exception.getMessage().contains("Internal server error"));
        assertNotNull(exception.getResponseBodyAsString());
        assertSchema(exception.getResponseBodyAsString(), "./data/schema/IPDataV1500Response.json");
    }
}
