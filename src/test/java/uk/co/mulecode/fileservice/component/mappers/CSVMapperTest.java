package uk.co.mulecode.fileservice.component.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import uk.co.mulecode.fileservice.model.PersonDataIngestionDto;
import uk.co.mulecode.fileservice.utils.UnitTestUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVMapperTest extends UnitTestUtils {

    private final uk.co.mulecode.fileservice.component.mappers.CSVMapper CSVMapper = new CSVMapper();

    @Test
    void mapToCSV_ValidFile_BeansParsed() throws Exception {
        // given
        String fileName = "data/entry_valid.txt";
        MockMultipartFile file = readFileAsMockMultipartFile(fileName);
        // when
        List<PersonDataIngestionDto> persons = CSVMapper.csvToBean(PersonDataIngestionDto.class, file);
        // then
        assertEquals(3, persons.size());

        assertTrue(persons.contains(new PersonDataIngestionDto(UUID.fromString("18148426-89e1-11ee-b9d1-0242ac120002"), "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", 6.2, 12.1)));
        assertTrue(persons.contains(new PersonDataIngestionDto(UUID.fromString("3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7"), "2X2D24", "Mike Smith", "Likes Grape", "Drives an SUV", 35.0, 95.5)));
        assertTrue(persons.contains(new PersonDataIngestionDto(UUID.fromString("1afb6f5d-a7c2-4311-a92d-974f3180ff5e"), "3X3D35", "Jenny Walters", "Likes Avocados", "Rides A Scooter", 8.5, 15.3)));
    }
}
