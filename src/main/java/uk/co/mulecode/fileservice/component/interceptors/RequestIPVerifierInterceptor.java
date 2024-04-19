package uk.co.mulecode.fileservice.component.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.co.mulecode.fileservice.repository.IPDataRepository;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestIPVerifierInterceptor implements HandlerInterceptor {

    private final IPDataRepository ipDataRepository;
    private final Predicate<IPDataResponse> ipBlockingPolicy;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        long requestStartTime = System.currentTimeMillis();
        String ipAddress = request.getRemoteAddr();
        log.debug(">>>> preHandle - ipAddress: {} at {}", ipAddress, requestStartTime);
        IPDataResponse iPdata = ipDataRepository.getIPdata(ipAddress);
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
    }

}
