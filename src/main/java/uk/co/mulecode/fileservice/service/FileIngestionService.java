package uk.co.mulecode.fileservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mulecode.fileservice.component.mappers.CSVMapper;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.component.mappers.PersonDataMapper;
import uk.co.mulecode.fileservice.model.PersonDataIngestionDto;
import uk.co.mulecode.fileservice.model.PersonTransportationDto;

import java.util.List;

@Service
@AllArgsConstructor
public final class FileIngestionService {

    private final CSVMapper csvMapper;
    private final PersonDataMapper personDataMapper;
    private final JsonMapper jsonMapper;

    /**
     * Processes the content of a file.
     *
     * @param file the file to be processed.
     * @return the processed content in json format as String
     */
    public String processFile(MultipartFile file) {

        List<PersonDataIngestionDto> personDataIngestionDtos = csvMapper.csvToBean(PersonDataIngestionDto.class, file);

        List<PersonTransportationDto> mappedRecords = personDataIngestionDtos.stream()
                .map(personDataMapper::mapTo)
                .toList();

        return jsonMapper.toJson(mappedRecords);
    }
}
