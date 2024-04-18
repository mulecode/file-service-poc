package uk.co.mulecode.fileservice.component.exceptions;

import org.springframework.http.HttpStatus;
import uk.co.mulecode.fileservice.exception.ApplicationException;

public class CSVMapperException extends ApplicationException {

    private static final HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

    public CSVMapperException(String message) {
        super(message, httpStatus);
    }

    public CSVMapperException(String message, Throwable cause) {
        super(message, cause, httpStatus);
    }
}
