package exceptions;

public class InvalidCityException extends SimulationException {
    public InvalidCityException(String message) {
        super("Invalid City Configuration: " + message);
    }
}