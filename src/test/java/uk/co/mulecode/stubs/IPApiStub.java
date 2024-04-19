package uk.co.mulecode.stubs;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

;

public class IPApiStub {

    public static final String TEST_IP = "24.48.0.1";

    public static void stubSuccessApiResponse(String jsonBody) {
        stubFor(get(urlMatching("/json/(.*)\\?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonBody)));
    }

    public static void stubInternalServerApiResponse(String jsonBody) {
        stubFor(get(urlMatching("/json/(.*)\\?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonBody)));
    }

}
