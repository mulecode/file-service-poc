package uk.co.mulecode.fileservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.component.mappers.HttpRequestEntityMapper;
import uk.co.mulecode.fileservice.repository.HttpRequestRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpRequestEventHandler {

    private final HttpRequestRepository httpRequestRepository;
    private final HttpRequestEntityMapper httpRequestEntityMapper;

    @EventListener
    public void handleHttpRequestEvent(HttpRequestEvent event) {
        log.info("Handling event: {}", event.getEventName());

        httpRequestRepository.save(httpRequestEntityMapper.mapTo(event));

        log.debug("Event: {} saved with id {}", event.getEventName(), event.getRequestId());
    }
}
