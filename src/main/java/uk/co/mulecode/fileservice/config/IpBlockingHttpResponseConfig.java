package uk.co.mulecode.fileservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.co.mulecode.fileservice.component.mappers.JsonMapper;
import uk.co.mulecode.fileservice.controller.dto.ErrorResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class IpBlockingHttpResponseConfig {

    private final JsonMapper jsonMapper;

    public void responseBuilder(HttpServletRequest request, HttpServletResponse response, long requestTimestamp) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        HttpStatus httpStatusResponse = HttpStatus.FORBIDDEN;
        response.setStatus(httpStatusResponse.value());
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonMapper.toJson(ErrorResponse.builder()
                    .error(httpStatusResponse.name())
                    .status(httpStatusResponse.value())
                    .message("GeoLocation Policy Rule - Request was forbidden")
                    .exception("uk.co.mulecode.policy.GeoLocationPolicyException")
                    .path(request.getRequestURI())
                    .timestamp(requestTimestamp)
                    .build()));
        }
    }
}
