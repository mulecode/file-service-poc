package uk.co.mulecode.fileservice.component.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.co.mulecode.fileservice.component.exceptions.JsonParserException;

@Component
@RequiredArgsConstructor
public final class JsonMapper {

    private final ObjectMapper objectMapper;

    /**
     * Convert object or list of objects to json string.
     *
     * @param value object to convert
     * @return json string
     */
    public String toJson(final Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonParserException("Error converting object to json", e);
        }
    }
}
