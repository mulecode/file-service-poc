package uk.co.mulecode.fileservice.bdd.steps;

import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.mulecode.fileservice.bdd.SpringCucumberMvcContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class SpringTestInitialiserStepDefs {

    @Autowired
    SpringCucumberMvcContext springCucumberMvcContext;

    @Given("spring test initialiser")
    public void isSpringInitialised() throws Exception {

        assertNotNull(springCucumberMvcContext.getMvc());
    }

}
