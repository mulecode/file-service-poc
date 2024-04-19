package uk.co.mulecode.fileservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.co.mulecode.fileservice.repository.IPDataRepository;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Predicate;

import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.IP_API_DATA_ATR_NAME;
import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.REQUEST_ID_ATR_NAME;
import static uk.co.mulecode.fileservice.component.interceptors.RequestIPVerifierInterceptor.REQUEST_START_TIME_ATR_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInterceptorPreHandleConfig {

    private final IPDataRepository ipDataRepository;
    private final IpBlockingHttpResponseConfig ipBlockingHttpResponseConfig;
    private final Predicate<IPDataResponse> ipBlockingPolicy;
    private final RequestInterceptorCompletionConfig requestInterceptorCompletionConfig;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
            ipBlockingHttpResponseConfig.responseBuilder(request, response, requestStartTime);

            requestInterceptorCompletionConfig.afterCompletionHandler(request, response);
            return false;
        }
        return true;
    }


}
