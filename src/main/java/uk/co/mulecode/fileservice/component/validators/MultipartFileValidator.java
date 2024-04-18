package uk.co.mulecode.fileservice.component.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mulecode.fileservice.component.properties.AppProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class MultipartFileValidator implements ConstraintValidator<ValidIngestionFile, MultipartFile> {

    @Setter
    private Pattern csvLinePatternRegex;

    @Autowired
    private AppProperty appProperty;

    @Override
    public void initialize(ValidIngestionFile constraintAnnotation) {
        csvLinePatternRegex = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (!appProperty.getFeaturesFlags().isEnableCsvFileValidation()) {
            log.warn("CSV file validation is disabled");
            return true;
        }
        if (value.isEmpty()) {
            addConstraintViolation(context, "File cannot be empty for ingestion");
            return false;
        }
        if (!isValidCsv(value)) {
            addConstraintViolation(context, "Invalid CSV file format");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    private boolean isValidCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<String> lines = reader.lines().toList();
            // Check if there is at least one line in the CSV file
            if (lines.isEmpty()) {
                return false;
            }
            // Check if each line contains a valid CSV format
            for (String line : lines) {
                if (!isValidCsvLine(line)) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
            return false;
        }
    }

    private boolean isValidCsvLine(String line) {
        Matcher matcher = csvLinePatternRegex.matcher(line);
        return matcher.matches();
    }
}
