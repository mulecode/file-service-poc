package uk.co.mulecode.fileservice.component.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * Publishes an event to the application context.
     *
     * @param source the event to publish
     */
    public void publishEvent(Object source) {
        eventPublisher.publishEvent(source);
    }
}
