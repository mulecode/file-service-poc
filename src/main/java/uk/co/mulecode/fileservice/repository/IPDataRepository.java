package uk.co.mulecode.fileservice.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.co.mulecode.fileservice.component.properties.AppProperty;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public final class IPDataRepository {

    public static final String ALL_FIELDS_VALUE = "66846719";

    private final RestTemplate restTemplate;
    private final AppProperty appProperty;

    public IPDataResponse getIPdata(final String ip) {
        log.debug("Getting IP data for {}", ip);
        final String ipVerificationPartnerUrl = appProperty.getFeaturesConfigs().getIpVerificationPartnerUrl();
        final String url = format("%s%s?fields=%s", ipVerificationPartnerUrl, ip, ALL_FIELDS_VALUE);
        log.info("Making API call IP data to url: {}", url);
        final IPDataResponse response = restTemplate.getForObject(url, IPDataResponse.class);
        log.debug("IP data response: {}", response);
        return response;
    }
}
