package uk.co.mulecode.fileservice.component.validators;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.component.validators.patterns.RegexPatterns;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MultipartFileValidatorTest extends IntegrationTestBase {

    @Autowired
    private MultipartFileValidator multipartFileValidator;

    @BeforeEach
    void setUp() {
        // Inject the regex pattern for the CSV line
        multipartFileValidator.setCsvLinePatternRegex(
                Pattern.compile(RegexPatterns.CSV_LINE_PATTERN)
        );
    }

    private static Stream<TestIsValidOptions> isValidOptions() {
        return Stream.of(
                TestIsValidOptions.builder().filename("data/entry_valid.txt")
                        .expected(true)
                        .build(),
                TestIsValidOptions.builder().filename("data/entry_invalid_empty.txt")
                        .expected(false)
                        .errorMessage("File cannot be empty for ingestion")
                        .build(),
                TestIsValidOptions.builder().filename("data/entry_invalid.txt")
                        .expected(false)
                        .errorMessage("Invalid CSV file format")
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("isValidOptions")
    void isValid(TestIsValidOptions validOdds) throws Exception {
        ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);
        ConstraintValidatorContext.ConstraintViolationBuilder builder = mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        String fileName = validOdds.getFilename();

        MockMultipartFile file = readFileAsMockMultipartFile(fileName);

        boolean valid = multipartFileValidator.isValid(file, constraintValidatorContext);

        assertEquals(valid, validOdds.isExpected());

        if (!validOdds.isExpected()) {
            verify(builder).addConstraintViolation();
            verify(constraintValidatorContext).buildConstraintViolationWithTemplate(validOdds.getErrorMessage());
        }
    }

    @Data
    @Builder
    private static class TestIsValidOptions {
        String filename;
        boolean expected;
        String errorMessage;
        boolean disableCsvFileValidation;
    }
}
