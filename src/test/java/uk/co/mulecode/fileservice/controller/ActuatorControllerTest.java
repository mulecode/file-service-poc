package uk.co.mulecode.fileservice.controller;

import org.junit.jupiter.api.Test;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.mulecode.fileservice.utils.matchers.JsonSchemaValidatorResultMatcher.validateSchema;

class ActuatorControllerTest extends IntegrationTestBase {

    @Test
    void actuator_health_ReturnOk() throws Exception {

        getMvc().perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-type", "application/vnd.spring-boot.actuator.v3+json"))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(validateSchema("./data/schema/HealthV1Response.json"));
    }

}
