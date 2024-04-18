package uk.co.mulecode.fileservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Utility class for controller
 */
public class ControllerUtils {

    /**
     * Build headers for file response in the api
     *
     * @param responseBytes file bytes
     * @param filename      file name
     * @return HttpHeaders for api response
     */
    public HttpHeaders buildHeadersFileResponse(byte[] responseBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(responseBytes.length);
        headers.setContentDispositionFormData("attachment", filename);
        return headers;
    }
}
