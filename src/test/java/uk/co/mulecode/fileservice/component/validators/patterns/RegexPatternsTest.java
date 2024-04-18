package uk.co.mulecode.fileservice.component.validators.patterns;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegexPatternsTest {


    private static Stream<TestIsValidOptions> isValidOptions() {
        return Stream.of(
                TestIsValidOptions.builder()
                        .pattern(RegexPatterns.CSV_LINE_PATTERN)
                        .value("18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1")
                        .expected(true)
                        .build(),
                TestIsValidOptions.builder()
                        .pattern(RegexPatterns.CSV_LINE_PATTERN)
                        .value("")
                        .expected(false)
                        .build(),
                TestIsValidOptions.builder()
                        .pattern(RegexPatterns.CSV_LINE_PATTERN)
                        .value("-89e1-11e-b9d1-0242ac120002|||1X1D14|John Smith|Likes Apric Bike|6.2|12.1")
                        .expected(false)
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("isValidOptions")
    void testCSVPattern(TestIsValidOptions validOdds) {

        Matcher matcher = Pattern.compile(validOdds.getPattern())
                .matcher(validOdds.getValue());

        assertEquals(validOdds.isExpected(), matcher.matches());
    }

    @Data
    @Builder
    private static class TestIsValidOptions {
        String value;
        String pattern;
        boolean expected;
    }
}
