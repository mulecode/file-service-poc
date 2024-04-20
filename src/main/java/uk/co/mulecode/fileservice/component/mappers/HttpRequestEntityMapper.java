package uk.co.mulecode.fileservice.component.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.co.mulecode.fileservice.component.event.dto.HttpRequestEvent;
import uk.co.mulecode.fileservice.repository.dto.HttpRequestEntity;

import java.util.function.Function;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class HttpRequestEntityMapper {

    private static final Function<String, String> emptyWhenNull = (s) -> isNull(s) ? "" : s;

    /**
     * Maps the source HttpRequestEvent to the destination class HttpRequestEntity.
     *
     * @param source HttpRequestEvent the source object
     * @return HttpRequestEntity
     */
    public HttpRequestEntity mapTo(HttpRequestEvent source) {
        log.debug("Mapping HttpRequestEvent {} to HttpRequestEntity", source);
        return HttpRequestEntity.builder()
                .requestId(source.getRequestId())
                .requestUri(source.getRequest().getRequestURI())
                .requestTimestamp(source.getRequestTime())
                .responseHttpCode(source.getResponse().getStatus())
                .requestIpAddress(source.getRequest().getRemoteAddr())
                .requestCountryCode(emptyWhenNull.apply(source.getIpDataResponse().getCountryCode()))
                .requestIpProvider(emptyWhenNull.apply(source.getIpDataResponse().getIsp()))
                .timeLapsed(source.getTimeLapsed())
                .build();
    }

}
