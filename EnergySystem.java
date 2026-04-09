package systems;

import exceptions.*;
import java.util.*;

public class EnergySystem extends CitySystem implements SimulationSystem {
    private double powerUsage;
    private double powerProduction;
    private String gridStatus;
    private Random random;
    private static final double MAX_PRODUCTION = 1000.0;
    
    public EnergySystem(double initialProduction) 
            throws InvalidParameterException {
        super("Energy System", MAX_PRODUCTION);
        
        if (initialProduction <= 0 || initialProduction > MAX_PRODUCTION) {
            throw new InvalidParameterException("initialProduction", 
                    "Must be between 0 and " + MAX_PRODUCTION);
        }
        
        this.powerProduction = initialProduction;
        this.powerUsage = initialProduction * 0.75;
        this.gridStatus = "STABLE";
        this.random = new Random();
    }
    
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException {
        double usageVariation = 0.95 + random.nextDouble() * 0.15;
        double newUsage = powerProduction * usageVariation;
        
        this.powerUsage = Math.min(newUsage, powerProduction * 1.1);
        setCurrentLoad(powerUsage);
        
        updateGridStatus();
        
        if (getLoadPercentage() > 95) {
            throw new SystemOverloadException("Energy System", getLoadPercentage());
        }
    }
    
    private void updateGridStatus() {
        double usagePercent = (powerUsage / powerProduction) * 100;
        
        if (usagePercent < 70) {
            this.gridStatus = "STABLE";
        } else if (usagePercent < 85) {
            this.gridStatus = "STABLE";
        } else if (usagePercent < 95) {
            this.gridStatus = "WARNING";
        } else {
            this.gridStatus = "CRITICAL";
        }
    }
    
    public void addLoad(double load) throws InvalidParameterException {
        this.currentLoad = Math.min(currentLoad + load, maxCapacity * 1.2);
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        try {
            System.out.println("\n⚡ Energy System");
            System.out.println("-".repeat(40));
            update();
            System.out.printf("Electricity demand increased by %.1f%%.\n", 
                    (powerUsage / (powerProduction * 0.75) - 1) * 100);
            System.out.printf("Power grid operating at %.1f%% capacity.\n", 
                    (powerUsage / powerProduction) * 100);
        } catch (SystemOverloadException e) {
            System.err.println("⚠️  " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void handleOverload() {
        System.out.println("🔴 Implementing load shedding protocols.");
        this.gridStatus = "CRITICAL";
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "Power Usage             : %.1f%%\n" +
                "Power Production        : %.1f%%\n" +
                "Grid Status             : %s",
                (powerUsage / powerProduction) * 100,
                (powerProduction / MAX_PRODUCTION) * 100,
                gridStatus);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "ENERGY SYSTEM\n" +
                "-".repeat(40) + "\n" +
                "Electricity Usage       : %.1f%%\n" +
                "Power Production        : %.1f%%\n" +
                "Status                  : %s\n",
                (powerUsage / powerProduction) * 100,
                (powerProduction / MAX_PRODUCTION) * 100,
                gridStatus);
    }
    
    @Override
    public boolean isCritical() {
        return gridStatus.equals("CRITICAL");
    }
    
    @Override
    public void reset() {
        this.powerUsage = powerProduction * 0.75;
        this.gridStatus = "STABLE";
        this.currentLoad = 0;
        this.status = "STABLE";
    }
    
    public double getPowerUsage() {
        return powerUsage;
    }
    
    public double getPowerProduction() {
        return powerProduction;
    }
    
    public String getGridStatus() {
        return gridStatus;
    }
}