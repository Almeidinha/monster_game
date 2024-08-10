package co.fullstacklabs.battlemonsters.challenge.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @Test
    public void whenConstraintViolationException_thenValidationErrorIsReturned() {
        Set<ConstraintViolation<?>> mockViolations = new HashSet<>();
        ConstraintViolation<?> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);

        when(mockViolation.getMessage()).thenReturn("Mock violation message");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockPath.toString()).thenReturn("mockPathString");
        mockViolations.add(mockViolation);

        ConstraintViolationException ex = new ConstraintViolationException(mockViolations);

        WebRequest mockWebRequest = mock(WebRequest.class);
        when(mockWebRequest.getDescription(false)).thenReturn("web request description");

        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ValidationError result = handler.constraintViolationException(ex, mockWebRequest);

        assertEquals(1, result.getViolations().size());
    }

    @Test
    public void whenMethodArgumentNotValidException_thenValidationErrorIsReturned() {
        FieldError mockFieldError = new FieldError("objectName", "field", "Invalid field");
        BindingResult mockBindingResult = mock(BindingResult.class);
        List<FieldError> t = new java.util.ArrayList<>();
        t.add(mockFieldError);
        when(mockBindingResult.getFieldErrors()).thenReturn(t);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, mockBindingResult);

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ValidationError result = handler.constraintViolationException(ex);

        assertNotNull(result, "The ValidationError should not be null");
        assertEquals(1, result.getViolations().size(), "There should be one violation error");
        assertEquals("Invalid field", result.getViolations().get(0).getMessage(), "The error message should match");
    }

    @Test
    public void whenUnprocessableFileException_thenErrorDetailsAreReturned() {
        UnprocessableFileException ex = mock(UnprocessableFileException.class);
        when(ex.getMessage()).thenReturn("Unprocessable file error");

        WebRequest mockWebRequest = mock(WebRequest.class);
        when(mockWebRequest.getDescription(false)).thenReturn("web request description");

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ErrorDetails result = handler.unprocessedFileException(ex, mockWebRequest);

        assertNotNull(result, "The ErrorDetails should not be null");
        assertEquals("Unprocessable file error", result.getMessage(), "The error message should match");
        assertEquals("web request description", result.getDetails(), "The request description should match");
    }


}

