package exceptions;

public class SystemOverloadException extends SimulationException {
    public SystemOverloadException(String systemName, double currentLoad) {
        super(systemName + " is overloaded at " + currentLoad + "%");
    }
}
