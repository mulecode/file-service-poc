package uk.co.mulecode.fileservice.component.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.co.mulecode.fileservice.component.event.EventProducer;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.repository.IPDataRepository;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestIPVerifierInterceptor implements HandlerInterceptor {

    public static final String IP_API_DATA_ATR_NAME = "ipApiDataByIPAddress";
    public static final String REQUEST_START_TIME_ATR_NAME = "requestStartTime";
    public static final String REQUEST_ID_ATR_NAME = "requestId";
    private final IPDataRepository ipDataRepository;
    private final Predicate<IPDataResponse> ipBlockingPolicy;
    private final EventProducer eventProducer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long requestStartTime = System.currentTimeMillis();
        request.setAttribute(REQUEST_START_TIME_ATR_NAME, requestStartTime);
        String ipAddress = request.getRemoteAddr();

        UUID requestId = UUID.randomUUID();
        request.setAttribute(REQUEST_ID_ATR_NAME, requestId);
        log.debug(">>>> preHandle - ipAddress: {} at {}", ipAddress, requestStartTime);
        IPDataResponse iPdata = ipDataRepository.getIPdata(ipAddress);
        request.setAttribute(IP_API_DATA_ATR_NAME, iPdata);

        if (ipBlockingPolicy.test(iPdata)) {
            log.debug(">>>> preHandle - ipAddress blocked: {} Income connection policy rule activated for ip data {}", ipAddress, iPdata);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        log.debug(">>>> postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        log.debug(">>>> afterCompletion");
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
        log.debug(">>>> afterCompletion - completed");
    }

}
