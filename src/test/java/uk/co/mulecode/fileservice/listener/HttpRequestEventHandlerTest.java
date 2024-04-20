package uk.co.mulecode.fileservice.listener;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import uk.co.mulecode.fileservice.component.event.EventProducer;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.component.mappers.HttpRequestEntityMapper;
import uk.co.mulecode.fileservice.repository.HttpRequestRepository;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HttpRequestEventHandlerTest extends IntegrationTestBase {

    @Autowired
    EventProducer eventProducer;
    @MockBean
    HttpRequestRepository httpRequestRepository;
    @MockBean
    HttpRequestEntityMapper httpRequestEntityMapper;

    @Test
    void handleHttpRequestEvent() {
        // Given
        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        MockHttpServletResponse response = mock(MockHttpServletResponse.class);
        final HttpRequestEvent httpRequestEvent = HttpRequestEvent.builder()
                .requestId(UUID.randomUUID())
                .request(request)
                .requestTime(System.currentTimeMillis())
                .eventName("test")
                .response(response)
                .timeLapsed(10L)
                .build();

        final HttpRequestEntity httpRequestEntity = givenOneObjectOf(HttpRequestEntity.class);
        when(httpRequestEntityMapper.mapTo(any())).thenReturn(httpRequestEntity);
        when(httpRequestRepository.save(any())).thenReturn(httpRequestEntity);
        // When
        eventProducer.publishEvent(httpRequestEvent);
        // Then
        verify(httpRequestEntityMapper).mapTo(eq(httpRequestEvent));
        verify(httpRequestRepository).save(eq(httpRequestEntity));
    }

}
