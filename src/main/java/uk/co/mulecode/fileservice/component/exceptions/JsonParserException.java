package uk.co.mulecode.fileservice.component.exceptions;

import org.springframework.http.HttpStatus;
import uk.co.mulecode.fileservice.exception.ApplicationException;

public class JsonParserException extends ApplicationException {

    private static final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    public JsonParserException(String message) {
        super(message, httpStatus);
    }

    public JsonParserException(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }
}
