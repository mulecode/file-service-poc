package uk.co.mulecode.fileservice.component.interceptors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.co.mulecode.fileservice.repository.IPDataRepository;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.co.mulecode.stubs.IPApiStub.TEST_IP;

class RequestVerifierInterceptorTest extends IntegrationTestBase {

    @Autowired
    private RequestIPVerifierInterceptor interceptor;

    @MockBean
    private IPDataRepository ipDataRepository;

    @Test
    public void preHandle_GetRemoteAddress() throws Exception {
        // given
        IPDataResponse ipDataResponse = givenOneObjectOf(IPDataResponse.class);
        when(ipDataRepository.getIPdata(TEST_IP)).thenReturn(ipDataResponse);

        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn(TEST_IP);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        // when
        interceptor.preHandle(request, response, handler);
        // then
        verify(request, times(1)).getRemoteAddr();
        verify(ipDataRepository, times(1)).getIPdata(TEST_IP);
    }
}
