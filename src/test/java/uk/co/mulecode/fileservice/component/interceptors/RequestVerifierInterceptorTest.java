package uk.co.mulecode.fileservice.component.interceptors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.co.mulecode.fileservice.component.event.EventProducer;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.repository.IPDataRepository;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RequestVerifierInterceptorTest extends IntegrationTestBase {

    @Autowired
    private RequestIPVerifierInterceptor interceptor;

    @MockBean
    private IPDataRepository ipDataRepository;

    @MockBean
    private EventProducer eventProducer;

    @Test
    public void preHandle_GetRemoteAddress() throws Exception {
        // given
        String addressIp = getFaker().internet().ipV4Address();
        IPDataResponse ipDataResponse = givenOneObjectOf(IPDataResponse.class);
        when(ipDataRepository.getIPdata(addressIp)).thenReturn(ipDataResponse);

        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn(addressIp);
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        // when
        interceptor.preHandle(request, response, handler);
        // then
        verify(request, times(1)).getRemoteAddr();
        verify(ipDataRepository, times(1)).getIPdata(addressIp);
    }

    @Test
    public void afterCompletion_PublishEvent() throws Exception {
        // given
        IPDataResponse ipDataResponse = givenOneObjectOf(IPDataResponse.class);
        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        when(request.getAttribute(RequestIPVerifierInterceptor.IP_API_DATA_ATR_NAME)).thenReturn(ipDataResponse);
        when(request.getAttribute(RequestIPVerifierInterceptor.REQUEST_START_TIME_ATR_NAME)).thenReturn(System.currentTimeMillis());
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();
        // when
        interceptor.afterCompletion(request, response, handler, null);
        // then

        ArgumentCaptor<HttpRequestEvent> eventCaptor = ArgumentCaptor.forClass(HttpRequestEvent.class);
        verify(eventProducer).publishEvent(eventCaptor.capture());

        HttpRequestEvent capturedEvent = eventCaptor.getValue();
        assertEquals("IPDataRequestEvent", capturedEvent.getEventName());
        assertEquals(ipDataResponse, capturedEvent.getIpDataResponse());
        assertEquals(request, capturedEvent.getRequest());
        assertNotNull(capturedEvent.getTimeLapsed());
    }
}
