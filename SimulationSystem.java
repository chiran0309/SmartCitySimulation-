package systems;

import exceptions.*;

public interface SimulationSystem {
    void simulateOneDay() throws SimulationException;
    String getReport();
    boolean isCritical();
    void reset();
}