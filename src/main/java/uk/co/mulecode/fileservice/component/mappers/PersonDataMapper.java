package uk.co.mulecode.fileservice.component.mappers;

import org.springframework.stereotype.Service;
import uk.co.mulecode.fileservice.model.PersonDataIngestionDto;
import uk.co.mulecode.fileservice.model.PersonTransportationDto;

@Service
public class PersonDataMapper {

    /**
     * Maps the source PersonDataIngestionDto to the destination class PersonTransportationDto.
     *
     * @param source PersonDataIngestionDto the source object
     * @return PersonTransportationDto
     */
    public PersonTransportationDto mapTo(PersonDataIngestionDto source) {
        return PersonTransportationDto.builder()
                .name(source.getName())
                .transport(source.getTransport())
                .topSpeed(source.getTopSpeed())
                .build();
    }

}
