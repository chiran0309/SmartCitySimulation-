package exceptions;

public class InvalidParameterException extends SimulationException {
    public InvalidParameterException(String paramName, String reason) {
        super("Invalid Parameter '" + paramName + "': " + reason);
    }
}