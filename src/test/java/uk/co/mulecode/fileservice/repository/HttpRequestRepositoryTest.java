package uk.co.mulecode.fileservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;
import uk.co.mulecode.fileservice.test.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class HttpRequestRepositoryTest extends IntegrationTestBase {

    @Autowired
    HttpRequestRepository httpRequestRepository;

    @Test
    void save_ValidEntity_ShouldSave() {
        // Given
        HttpRequestEntity httpRequestEntity = givenOneObjectOf(HttpRequestEntity.class);
        // When
        var savedEntity = httpRequestRepository.save(httpRequestEntity);
        // Then
        assertNotNull(savedEntity);
        httpRequestRepository.findById(savedEntity.getRequestId()).ifPresentOrElse(
                entity -> assertEquals(savedEntity, entity),
                () -> fail("Entity not found")
        );
    }
}
