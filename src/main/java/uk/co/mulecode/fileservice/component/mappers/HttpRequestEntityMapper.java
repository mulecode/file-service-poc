package uk.co.mulecode.fileservice.component.mappers;

import org.springframework.stereotype.Service;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;

@Service
public class HttpRequestEntityMapper {

    /**
     * Maps the source HttpRequestEvent to the destination class HttpRequestEntity.
     *
     * @param source HttpRequestEvent the source object
     * @return HttpRequestEntity
     */
    public HttpRequestEntity mapTo(HttpRequestEvent source) {
        return HttpRequestEntity.builder()
                .requestId(source.getRequestId())
                .requestUri(source.getRequest().getRequestURI())
                .requestTimestamp(source.getRequestTime())
                .responseHttpCode(source.getResponse().getStatus())
                .requestIpAddress(source.getRequest().getRemoteAddr())
                .requestCountryCode(source.getIpDataResponse().getCountryCode())
                .requestIpProvider(source.getIpDataResponse().getIsp())
                .timeLapsed(source.getTimeLapsed())
                .build();
    }

}
