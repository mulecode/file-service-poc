package uk.co.mulecode.fileservice.component.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import uk.co.mulecode.fileservice.config.RequestInterceptorCompletionConfig;
import uk.co.mulecode.fileservice.config.RequestInterceptorPreHandleConfig;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestIPVerifierInterceptor implements HandlerInterceptor {

    public static final String IP_API_DATA_ATR_NAME = "ipApiDataByIPAddress";
    public static final String REQUEST_START_TIME_ATR_NAME = "requestStartTime";
    public static final String REQUEST_ID_ATR_NAME = "requestId";

    private final RequestInterceptorCompletionConfig requestInterceptorCompletionConfig;
    private final RequestInterceptorPreHandleConfig requestInterceptorPreHandleConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug(">>>> preHandle");
        return requestInterceptorPreHandleConfig.preHandle(request, response);
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
        requestInterceptorCompletionConfig.afterCompletionHandler(request, response);
    }

}
