package uk.co.mulecode.fileservice.utils;

import com.github.javafaker.Faker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import org.jeasy.random.EasyRandom;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UnitTestUtils {

    private final Faker faker = Faker.instance();

    private final EasyRandom easyRandom = new EasyRandom();

    public UUID givenUUID() {
        return UUID.randomUUID();
    }

    public <T> T givenOneObjectOf(Class<T> type) {
        return easyRandom.nextObject(type);
    }

    public <T> List<T> givenListOf(Class<T> type, int size) {
        return easyRandom.objects(type, size).collect(Collectors.toList());
    }

    public <T> Optional<T> givenEmptyOptional(Class<T> type) {
        return Optional.empty();
    }

    public <T> Optional<T> givenOptionalOf(Class<T> type) {
        return Optional.of(givenOneObjectOf(type));
    }

    public <T> Set<ConstraintViolation<T>> validateConstraings(T obj) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(obj);
    }

    public Path getResourcePath(String fileName) {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
    }

    public String readFileAsString(String fileName) throws IOException {
        return Files.readString(getResourcePath(fileName));
    }

    public byte[] readFileAsBytes(String fileName) throws IOException {
        return Files.readAllBytes(getResourcePath(fileName));
    }

    public MockMultipartFile readFileAsMockMultipartFile(String fileName) throws IOException {
        byte[] fileContent = readFileAsBytes(fileName);
        return new MockMultipartFile("file", fileName, "text/plain", fileContent);
    }
}
