package uk.co.mulecode.fileservice.component.mappers;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mulecode.fileservice.component.exceptions.CSVMapperException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public final class CSVMapper {

    /**
     * Maps the content of a CSV file to a list of beans of the specified class type.
     *
     * @param classType the class type of the beans to be created.
     * @param file the CSV file to be read.
     * @return a list of beans of the specified class type.
     */
    public <T> List<T> csvToBean(Class<T> classType, MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            return new CsvToBeanBuilder<T>(reader)
                    .withType(classType)
                    .withSeparator('|')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new CSVMapperException("Error converting CSV to Bean", e);
        }
    }
}
