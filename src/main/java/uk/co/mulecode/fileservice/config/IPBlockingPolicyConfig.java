package uk.co.mulecode.fileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.util.function.Predicate;

import static java.util.Objects.isNull;

@Configuration
public class IPBlockingPolicyConfig {

    @Bean
    public Predicate<IPDataResponse> ipBlockingPolicy() {
        return ipDataResponse ->
                isNull(ipDataResponse.getCountryCode()) ||
                        isNull(ipDataResponse.getIsp()) ||
                        ipDataResponse.getCountryCode().equalsIgnoreCase("CN") ||
                        ipDataResponse.getCountryCode().equalsIgnoreCase("ES") ||
                        ipDataResponse.getCountryCode().equalsIgnoreCase("US") ||
                        ipDataResponse.getIsp().equalsIgnoreCase("AWS") ||
                        ipDataResponse.getIsp().equalsIgnoreCase("GCP") ||
                        ipDataResponse.getIsp().equalsIgnoreCase("Azure");
    }
}
