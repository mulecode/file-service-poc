package uk.co.mulecode.fileservice.component.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.mulecode.fileservice.model.PersonTransportationDto;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import static com.jayway.jsonpath.JsonPath.read;
import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonMapperTest extends IntegrationTestBase {

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void toJson_ValidDto_ReturnJson() {
        // given
        var personTransportationDto = givenOneObjectOf(PersonTransportationDto.class);
        // when
        var json = jsonMapper.toJson(personTransportationDto);
        // then
        assertEquals(personTransportationDto.getName(), read(json, "$.name"));
        assertEquals(personTransportationDto.getTransport(), read(json, "$.transport"));
        assertEquals(personTransportationDto.getTopSpeed(), read(json, "$.topSpeed"));
    }

    @Test
    void toJson_ValidListDto_ReturnArrayJson() {
        // given
        var personTransportationDtos = givenListOf(PersonTransportationDto.class, 1);
        // when
        var json = jsonMapper.toJson(personTransportationDtos);
        // then
        var first = personTransportationDtos.get(0);
        var size = read(json, "$.length()");
        assertEquals(1, size);
        assertEquals(first.getName(), read(json, "$[0].name"));
        assertEquals(first.getTransport(), read(json, "$[0].transport"));
        assertEquals(first.getTopSpeed(), read(json, "$[0].topSpeed"));
    }
}
