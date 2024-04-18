package uk.co.mulecode.fileservice.component.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClockUtilsTest {

    @Test
    void getNowTimestamp() {
        // given
        ClockUtils clockUtils = new ClockUtils();
        // when
        long timestamp = clockUtils.getNowTimestamp();
        // then
        long currentTimestamp = System.currentTimeMillis();
        assertTrue(timestamp <= currentTimestamp);
    }
}
