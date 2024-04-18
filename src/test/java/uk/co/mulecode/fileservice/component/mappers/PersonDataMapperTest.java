package uk.co.mulecode.fileservice.component.mappers;

import org.junit.jupiter.api.Test;
import uk.co.mulecode.fileservice.model.PersonDataIngestionDto;
import uk.co.mulecode.fileservice.utils.UnitTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonDataMapperTest extends UnitTestUtils {

    private final PersonDataMapper mapper = new PersonDataMapper();

    @Test
    void personDataMapper_validSource_TargetMapped() {
        // given
        var personData = givenOneObjectOf(PersonDataIngestionDto.class);
        // when
        var personTransportation = mapper.mapTo(personData);
        // then
        assertEquals(personData.getName(), personTransportation.getName());
        assertEquals(personData.getTransport(), personTransportation.getTransport());
        assertEquals(personData.getTopSpeed(), personTransportation.getTopSpeed());
    }
}
