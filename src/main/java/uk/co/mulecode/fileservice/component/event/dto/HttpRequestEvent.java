package uk.co.mulecode.fileservice.component.event.dto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;
import uk.co.mulecode.fileservice.repository.dto.IPDataResponse;

import java.util.UUID;


@Data
@Builder
public class HttpRequestEvent {
    private String eventName;
    private UUID requestId;
    private long requestTime;
    private IPDataResponse ipDataResponse;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private long timeLapsed;
}
