package uk.co.mulecode.fileservice.component.interceptors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class RequestVerifierInterceptorTest {

    private final RequestIPVerifierInterceptor interceptor = new RequestIPVerifierInterceptor();

    @Test
    public void preHandle_GetRemoteAddress() throws Exception {
        // given
        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        // when
        interceptor.preHandle(request, response, handler);
        // then
        Mockito.verify(request, times(1)).getRemoteAddr();
    }
}
