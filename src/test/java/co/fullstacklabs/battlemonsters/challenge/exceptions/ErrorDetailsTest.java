package co.fullstacklabs.battlemonsters.challenge.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDetailsTest {
    @Test
    public void testErrorDetails() {
        String testMessage = "Test Message";
        String testDetails = "Test Details";

        ErrorDetails errorDetails = new ErrorDetails(testMessage, testDetails);

        assertEquals(testMessage, errorDetails.getMessage(), "The message should be correctly set by constructor");
        assertEquals(testDetails, errorDetails.getDetails(), "The details should be correctly set by constructor");

        String newTestMessage = "New Test Message";
        String newTestDetails = "New Test Details";
        errorDetails.setMessage(newTestMessage);
        errorDetails.setDetails(newTestDetails);

        assertEquals(newTestMessage, errorDetails.getMessage(), "The message should be correctly updated by setter");
        assertEquals(newTestDetails, errorDetails.getDetails(), "The details should be correctly updated by setter");
    }
}
