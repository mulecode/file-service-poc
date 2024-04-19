package uk.co.mulecode.fileservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.co.mulecode.fileservice.component.event.EventProducer;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.util.UUID;

import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.IP_API_DATA_ATR_NAME;
import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.REQUEST_ID_ATR_NAME;
import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.REQUEST_START_TIME_ATR_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptorCompletionConfig {

    private final EventProducer eventProducer;

    public void afterCompletionHandler(HttpServletRequest request, HttpServletResponse response) {
        final IPDataResponse ipData = (IPDataResponse) request.getAttribute(IP_API_DATA_ATR_NAME);
        long requestStartTime = (long) request.getAttribute(REQUEST_START_TIME_ATR_NAME);
        long timeLapsed = requestStartTime - System.currentTimeMillis();

        UUID requestId = (UUID) request.getAttribute(REQUEST_ID_ATR_NAME);

        log.debug(">>>> afterCompletion - publishing event IPDataRequestEvent");
        eventProducer.publishEvent(
                HttpRequestEvent.builder()
                        .eventName("IPDataRequestEvent")
                        .requestId(requestId)
                        .requestTime(requestStartTime)
                        .ipDataResponse(ipData)
                        .request(request)
                        .response(response)
                        .timeLapsed(timeLapsed)
                        .build()
        );
    }
}
