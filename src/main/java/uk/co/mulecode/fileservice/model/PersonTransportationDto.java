package uk.co.mulecode.fileservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * Represents a person transportation DTO transformed from the file ingested.
 */
@Value
@Builder
@AllArgsConstructor
public class PersonTransportationDto {
    String name;
    String transport;
    Double topSpeed;
}
