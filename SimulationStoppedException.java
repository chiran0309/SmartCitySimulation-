package exceptions;

public class SimulationStoppedException extends SimulationException {
    public SimulationStoppedException(String message) {
        super("Simulation Stopped: " + message);
    }
}