package uk.co.mulecode.fileservice.config;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IPBlockingPolicyConfigTest extends IntegrationTestBase {

    @Autowired
    Predicate<IPDataResponse> ipBlockingPolicy;

    private static Stream<TestIsValidOptions> isValidOptions() {
        return Stream.of(
                testBuilder("CN", "AT&T", true),
                testBuilder("ES", "AT&T", true),
                testBuilder("US", "AT&T", true),
                testBuilder(null, "AT&T", true),
                testBuilder("UK", "AWS", true),
                testBuilder("UK", "GCP", true),
                testBuilder("UK", "Azure", true),
                testBuilder("UK", null, true),
                testBuilder(null, null, true),
                testBuilder("BR", "Atlantic Broadband", false),
                testBuilder("UK", "Google Fiber", false),
                testBuilder("IT", "AT&T", false)
        );
    }

    @ParameterizedTest
    @MethodSource("isValidOptions")
    void ipBlockingPolicy(TestIsValidOptions validOdds) {
        final boolean isIpBlocked = ipBlockingPolicy.test(validOdds.getValue());
        assertEquals(validOdds.isExpected(), isIpBlocked);
    }

    @Data
    @Builder
    private static class TestIsValidOptions {
        IPDataResponse value;
        boolean expected;
    }

    private static TestIsValidOptions testBuilder(String countryCode, String ips, boolean expected) {
        return TestIsValidOptions.builder()
                .value(IPDataResponse.builder()
                        .isp(ips)
                        .countryCode(countryCode)
                        .build())
                .expected(expected)
                .build();
    }

}
