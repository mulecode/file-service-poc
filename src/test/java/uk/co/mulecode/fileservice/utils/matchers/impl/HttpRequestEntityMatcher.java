package uk.co.mulecode.fileservice.utils.matchers.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import uk.co.mulecode.fileservice.repository.HttpRequestRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Profile("test")
@Component
@RequiredArgsConstructor
public class HttpRequestEntityMatcher {

    private final HttpRequestRepository httpRequestRepository;

    public void assertExistsByRequestIpAddress(String requestIpAddress) throws AssertionError {
        httpRequestRepository.findByRequestIpAddress(requestIpAddress).ifPresentOrElse(
                httpRequestEntity -> {
                    assertEquals(requestIpAddress, httpRequestEntity.getRequestIpAddress());
                },
                () -> {
                    throw new AssertionError("Entity not found by " + requestIpAddress);
                }
        );
    }
}
