package uk.co.mulecode.fileservice.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.co.mulecode.fileservice.test.UnitTestUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AutoConfigureWireMock(port = 9090, stubs = "classpath:/stubs")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("bdd")
public class SpringCucumberMvcContext extends UnitTestUtils {

    @Autowired
    private WebApplicationContext context;

    @Getter
    private MockMvc mvc;

    @Getter
    private Map<String, Object> params = new HashMap<>();

    @PostConstruct
    public void setup() {
        log.info("Setting up SpringCucumberMvcContext");
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

}
