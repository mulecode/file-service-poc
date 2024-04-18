package uk.co.mulecode.fileservice.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

/**
 * Represents a person data ingestion DTO from file ingestion.
 */
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class PersonDataIngestionDto {
    @CsvBindByPosition(position = 0)
    UUID id;
    @CsvBindByPosition(position = 1)
    String code;
    @CsvBindByPosition(position = 2)
    String name;
    @CsvBindByPosition(position = 3)
    String likes;
    @CsvBindByPosition(position = 4)
    String transport;
    @CsvBindByPosition(position = 5)
    Double avgSpeed;
    @CsvBindByPosition(position = 6)
    Double topSpeed;
}
