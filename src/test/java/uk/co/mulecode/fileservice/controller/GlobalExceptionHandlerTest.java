package uk.co.mulecode.fileservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uk.co.mulecode.fileservice.component.utils.ClockUtils;
import uk.co.mulecode.fileservice.controller.dto.ErrorResponse;
import uk.co.mulecode.fileservice.exception.ApplicationException;
import uk.co.mulecode.fileservice.utils.IntegrationTestBase;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest extends IntegrationTestBase {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private ClockUtils clockUtils;

    @MockBean
    private HttpServletRequest mockedRequest;

    @BeforeEach
    void setUp() {
        when(clockUtils.getNowTimestamp()).thenReturn(1234567890L);
        when(mockedRequest.getRequestURI()).thenReturn("/loren/ipsum");
    }

    @Test
    void applicationException() {
        // given
        ApplicationException applicationException = new ApplicationException("message", HttpStatus.INTERNAL_SERVER_ERROR);
        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.applicationException(applicationException, mockedRequest);
        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", body.getError());
        assertEquals("uk.co.mulecode.fileservice.exception.ApplicationException", body.getException());
        assertEquals("Application error - message", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
    }

    @Test
    void methodArgumentTypeMismatchException() {
        // given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("name");
        when(exception.getMessage()).thenReturn("message");
        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.methodArgumentTypeMismatchException(exception, mockedRequest);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("BAD_REQUEST", body.getError());
        assertEquals("org.springframework.web.method.annotation.MethodArgumentTypeMismatchException", body.getException());
        assertEquals("Validation failed, error count: 1", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
    }

    @Test
    void bindException() {
        // given
        FieldError error1 = mockFieldError();

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(List.of(error1));
        BindException bindException = new BindException(bindingResult);
        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.bindException(bindException, mockedRequest);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("BAD_REQUEST", body.getError());
        assertEquals("org.springframework.validation.BindException", body.getException());
        assertEquals("Validation failed, error count: 1", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
        assertEquals(1, body.getErrors().size());
        assertEquals(Map.of("field 1", "Default message"), body.getErrors());
    }

    @Test
    void handleConstraintViolationException() {
        // given
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        final Path.Node node = mockNode();
        final Iterator<Path.Node> nodeIterator = mockIterator(node);
        final Path nodes = mockPropertyPath(nodeIterator);
        when(violation.getPropertyPath()).thenReturn(nodes);
        when(violation.getMessage()).thenReturn("Violation message");
        when(exception.getConstraintViolations()).thenReturn(Set.of(violation));

        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception, mockedRequest);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("BAD_REQUEST", body.getError());
        assertEquals("jakarta.validation.ConstraintViolationException", body.getException());
        assertEquals("Validation failed, error count: 1", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
        assertEquals(1, body.getErrors().size());
    }

    @Test
    void handleArgumentNotValidException() {
        // given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = mockFieldError();
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(error1));
        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleArgumentNotValidException(exception, mockedRequest);
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("BAD_REQUEST", body.getError());
        assertEquals("org.springframework.web.bind.MethodArgumentNotValidException", body.getException());
        assertEquals("Validation failed, error count: 1", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
        assertEquals(1, body.getErrors().size());
        assertEquals(Map.of("field 1", "Default message"), body.getErrors());
    }

    @Test
    void genericException() {
        // given
        Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn("Error message");
        // when
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.genericException(exception, mockedRequest);
        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("INTERNAL_SERVER_ERROR", body.getError());
        assertEquals("java.lang.Exception", body.getException());
        assertEquals("Unhandled error exception - Error message", body.getMessage());
        assertEquals("/loren/ipsum", body.getPath());
        assertEquals(1234567890L, body.getTimestamp());
    }

    private Path mockPropertyPath(Iterator<Path.Node> iterator) {
        Path propertyPath = mock(Path.class);
        when(propertyPath.iterator()).thenReturn(iterator);
        return propertyPath;
    }

    private Iterator<Path.Node> mockIterator(Path.Node node) {
        Iterator<Path.Node> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true, false); // Simulate hasNext() returning true once, then false
        when(iterator.next()).thenReturn(node); // Simulate next() returning the mocked Path.Node
        return iterator;
    }

    private Path.Node mockNode() {
        Path.Node node = mock(Path.Node.class);
        when(node.getName()).thenReturn("field 1");
        return node;
    }

    private FieldError mockFieldError() {
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getObjectName()).thenReturn("objectName");
        when(fieldError.getField()).thenReturn("field 1");
        when(fieldError.getDefaultMessage()).thenReturn("Default message");
        return fieldError;
    }
}
