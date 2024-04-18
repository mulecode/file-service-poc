package uk.co.mulecode.fileservice.component.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppPropertyTest extends IntegrationTestBase {

    @Autowired
    private AppProperty appProperty;

    @Test
    void getName() {
        final String name = appProperty.getName();
        assertEquals("file-service-poc-test", name);
    }
}
