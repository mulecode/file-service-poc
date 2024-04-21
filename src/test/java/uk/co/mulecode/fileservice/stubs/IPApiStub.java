package uk.co.mulecode.fileservice.stubs;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public class IPApiStub {

    private static void stubSuccessApiResponse(String jsonBody) {
        stubFor(get(urlMatching("/json/(.*)\\?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonBody)));
    }

    private static void stubInternalServerApiResponse(String jsonBody) {
        stubFor(get(urlMatching("/json/(.*)\\?fields=66846719"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonBody)));
    }

}
