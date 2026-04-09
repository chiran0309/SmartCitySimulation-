package systems;

import exceptions.*;
import java.util.*;

public class WaterSystem extends CitySystem implements SimulationSystem {
    private double waterConsumption;
    private double waterSupplyCapacity;
    private Random random;
    private static final double MAX_WATER = 500.0;
    
    public WaterSystem(double initialSupply) 
            throws InvalidParameterException {
        super("Water System", MAX_WATER);
        
        if (initialSupply <= 0 || initialSupply > MAX_WATER) {
            throw new InvalidParameterException("initialSupply", 
                    "Must be between 0 and " + MAX_WATER);
        }
        
        this.waterSupplyCapacity = initialSupply;
        this.waterConsumption = initialSupply * 0.60;
        this.random = new Random();
    }
    // Add these methods to WaterSystem

    public void addLoad(double load) throws InvalidParameterException {
    this.currentLoad = Math.min(currentLoad + load, maxCapacity * 1.2);
    }

    public String getStatus() {
    return status;
    }
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException {
        double consumptionVariation = 0.98 + random.nextDouble() * 0.08;
        double newConsumption = waterConsumption * consumptionVariation;
        
        this.waterConsumption = Math.min(newConsumption, waterSupplyCapacity);
        setCurrentLoad(waterConsumption);
        
        if (getLoadPercentage() > 95) {
            throw new SystemOverloadException("Water System", getLoadPercentage());
        }
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        try {
            System.out.println("\n💧 Water System");
            System.out.println("-".repeat(40));
            update();
            System.out.printf("Water consumption increased by %.1f%%.\n",
                    (waterConsumption / (waterSupplyCapacity * 0.60) - 1) * 100);
        } catch (SystemOverloadException e) {
            System.err.println("⚠️  " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void handleOverload() {
        System.out.println("🚨 Activating water conservation protocols.");
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "Water Consumption       : %.1f%%\n" +
                "Water Supply Capacity   : %.1f%%\n" +
                "Status                  : %s",
                (waterConsumption / waterSupplyCapacity) * 100,
                (waterSupplyCapacity / MAX_WATER) * 100,
                status);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "WATER SYSTEM\n" +
                "-".repeat(40) + "\n" +
                "Water Consumption       : %.1f%%\n" +
                "Water Capacity          : %.1f%%\n" +
                "Status                  : %s\n",
                (waterConsumption / waterSupplyCapacity) * 100,
                (waterSupplyCapacity / MAX_WATER) * 100,
                status);
    }
    
    @Override
    public boolean isCritical() {
        return status.equals("CRITICAL");
    }
    
    @Override
    public void reset() {
        this.waterConsumption = waterSupplyCapacity * 0.60;
        this.currentLoad = 0;
        this.status = "STABLE";
    }
    
    public double getWaterConsumption() {
        return waterConsumption;
    }
    
    public double getWaterSupplyCapacity() {
        return waterSupplyCapacity;
    }
}