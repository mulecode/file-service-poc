package uk.co.mulecode.fileservice.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.co.mulecode.fileservice.bdd.SpringCucumberMvcContext;
import uk.co.mulecode.fileservice.test.UnitTestUtils;
import uk.co.mulecode.fileservice.test.matchers.impl.HttpRequestEntityMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static uk.co.mulecode.fileservice.test.matchers.JsonSchemaValidatorResultMatcher.validateSchema;

@Slf4j
public class UploadCSVStepDefs extends UnitTestUtils {

    @Autowired
    private SpringCucumberMvcContext springCucumberMvcContext;

    @Autowired
    private HttpRequestEntityMatcher httpRequestEntityMatcher;

    @Given("A CSV file {string}")
    public void aCSVFile(String filename) throws Exception {
        MockMultipartFile file = readFileAsMockMultipartFile(filename);
        springCucumberMvcContext.getParams().put("file", file);
    }

    @When("I process the file from ip address {string}")
    public void iUploadTheCSVFileFromIpAddress(String idAddress) throws Exception {
        var file = (MockMultipartFile) springCucumberMvcContext.getParams().get("file");
        var result = springCucumberMvcContext.getMvc().perform(
                multipart("/process/upload")
                        .file(file).with(remoteAddr(idAddress)));
        springCucumberMvcContext.getParams().put("result", result);
    }

    @Then("I should receive {int} status code")
    public void iShouldReceivedStatusCode(int statusCode) throws Exception {
        var result = (ResultActions) springCucumberMvcContext.getParams().get("result");
        result.andExpect(MockMvcResultMatchers.status().is(statusCode));
    }

    @And("The response body should be valid with schema {string}")
    public void responseBodyJsonSchema(String schemaLocation) throws Exception {
        var result = (ResultActions) springCucumberMvcContext.getParams().get("result");
        result.andExpect(validateSchema(schemaLocation));
    }

    @And("A record with idAddress {string} should be saved in the database")
    public void validate(String idAddress) {
        httpRequestEntityMatcher.assertExistsByRequestIpAddress(idAddress);
    }
}
