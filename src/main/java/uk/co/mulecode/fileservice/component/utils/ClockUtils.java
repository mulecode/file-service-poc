package uk.co.mulecode.fileservice.component.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClockUtils {

    /**
     * Get current timestamp in milliseconds.
     *
     * @return current timestamp
     */
    public long getNowTimestamp() {
        return Instant.now().toEpochMilli();
    }
}
