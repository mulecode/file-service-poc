package uk.co.mulecode.fileservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uk.co.mulecode.fileservice.component.utils.ClockUtils;
import uk.co.mulecode.fileservice.component.utils.MessageLookup;
import uk.co.mulecode.fileservice.controller.dto.ErrorResponse;
import uk.co.mulecode.fileservice.exception.ApplicationException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageLookup messagesService;
    private final ClockUtils clockUtils;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e, HttpServletRequest request) {

        var errorTitle = messagesService.getMessage("error.application-error");

        var code = e.getHttpStatus();

        var bodyResponse = ErrorResponse.builder()
                .error(code.name())
                .status(code.value())
                .exception(e.getClass().getName())
                .message(format("%s - %s", errorTitle, e.getMessage()))
                .path(request.getRequestURI())
                .timestamp(clockUtils.getNowTimestamp())
                .build();

        log.error("{} - message: {}", errorTitle, e.getMessage(), e);

        return ResponseEntity.status(code).body(bodyResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        return defaultBadRequestResponse(
                List.of(new ObjectError(ex.getName(), ex.getMessage())),
                request.getRequestURI(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException ex, HttpServletRequest request) {

        return defaultBadRequestResponse(
                ex.getBindingResult().getAllErrors(),
                request.getRequestURI(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {

        var violation = ex.getConstraintViolations().iterator().next();
        var error = new ObjectError(violation.getPropertyPath().iterator().toString(), violation.getMessage());

        return defaultBadRequestResponse(
                List.of(error),
                request.getRequestURI(),
                ex.getClass().getName()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        return defaultBadRequestResponse(
                ex.getBindingResult().getAllErrors(),
                request.getRequestURI(),
                ex.getClass().getName()
        );
    }

    private ResponseEntity<ErrorResponse> defaultBadRequestResponse(List<ObjectError> errorsParam, String path, String exceptionName) {

        var code = HttpStatus.BAD_REQUEST;

        var errors = errorsParam.stream()
                .collect(Collectors.toMap(
                        e -> e instanceof FieldError ? ((FieldError) e).getField() : e.getObjectName(),
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        var bodyResponse = ErrorResponse.builder()
                .message(format("Validation failed, error count: %d", errors.size()))
                .exception(exceptionName)
                .status(code.value())
                .error(code.name())
                .errors(errors)
                .path(path)
                .timestamp(clockUtils.getNowTimestamp())
                .build();

        log.debug("Validation error - API: {}, exception: {}, details: {}",
                path,
                exceptionName,
                bodyResponse
        );

        return ResponseEntity.status(code).body(bodyResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> genericException(Exception e, HttpServletRequest request) {

        var errorTitle = messagesService.getMessage("error.application-unhandled-error");
        var code = HttpStatus.INTERNAL_SERVER_ERROR;

        var bodyResponse = ErrorResponse.builder()
                .error(code.name())
                .status(code.value())
                .exception(e.getClass().getName())
                .message(format("%s - %s", errorTitle, e.getMessage()))
                .path(request.getRequestURI())
                .timestamp(clockUtils.getNowTimestamp())
                .build();

        log.error("{} - {}", errorTitle, e.getMessage(), e);

        return ResponseEntity.status(code).body(bodyResponse);
    }

}
