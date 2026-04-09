package systems;

import exceptions.*;
import java.util.*;

public class TransportSystem extends CitySystem implements SimulationSystem {
    private int busesActive;
    private String trafficLevel;
    private double averageTravelTime;
    private double publicTransportLoad;
    private Random random;
    private static final int MAX_BUSES = 500;
    
    public TransportSystem(int initialBuses) 
            throws InvalidParameterException {
        super("Transport System", MAX_BUSES);
        
        if (initialBuses <= 0 || initialBuses > MAX_BUSES) {
            throw new InvalidParameterException("initialBuses", 
                    "Must be between 1 and " + MAX_BUSES);
        }
        
        this.busesActive = initialBuses;
        this.trafficLevel = "LOW";
        this.averageTravelTime = 20.0;
        this.publicTransportLoad = 0;
        this.random = new Random();
    }
    
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException {
        double congestionFactor = 0.85 + random.nextDouble() * 0.25;
        double newLoad = busesActive * congestionFactor;
        
        setCurrentLoad(newLoad);
        updateTrafficMetrics();
        
        if (getLoadPercentage() > 95) {
            throw new SystemOverloadException("Transport System", getLoadPercentage());
        }
    }
    
    private void updateTrafficMetrics() {
        double loadPercent = getLoadPercentage();
        
        if (loadPercent < 50) {
            this.trafficLevel = "LOW";
            this.averageTravelTime = 15 + random.nextDouble() * 10;
            this.publicTransportLoad = loadPercent * 0.8;
        } else if (loadPercent < 75) {
            this.trafficLevel = "MEDIUM";
            this.averageTravelTime = 25 + random.nextDouble() * 15;
            this.publicTransportLoad = loadPercent * 0.9;
        } else if (loadPercent < 90) {
            this.trafficLevel = "HIGH";
            this.averageTravelTime = 40 + random.nextDouble() * 15;
            this.publicTransportLoad = loadPercent * 0.95;
        } else {
            this.trafficLevel = "VERY HIGH";
            this.averageTravelTime = 50 + random.nextDouble() * 10;
            this.publicTransportLoad = Math.min(100, loadPercent);
        }
    }
    
    public void addBuses(int count) throws InvalidParameterException {
        int newTotal = this.busesActive + count;
        if (newTotal > MAX_BUSES) {
            throw new InvalidParameterException("buses", 
                    "Cannot exceed maximum capacity of " + MAX_BUSES);
        }
        this.busesActive = newTotal;
        System.out.println("✅ Added " + count + " buses. Total: " + busesActive);
    }
    
    public void addBuses(int count, String priority) throws InvalidParameterException {
        addBuses(count);
        System.out.println("🎯 Priority: " + priority);
    }
    
    public void addLoad(double load) throws InvalidParameterException {
        this.currentLoad = Math.min(currentLoad + load, maxCapacity * 1.2);
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        try {
            System.out.println("\n🚗 Transport System");
            System.out.println("-".repeat(40));
            update();
            System.out.println("Peak-hour congestion detected.");
            System.out.println("Traffic level increased to " + trafficLevel + ".");
            System.out.printf("Average travel time increased to %.0f minutes.\n", averageTravelTime);
        } catch (SystemOverloadException e) {
            System.err.println("⚠️  " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void handleOverload() {
        System.out.println("🚨 Implementing emergency traffic management protocols.");
        this.trafficLevel = "CRITICAL";
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "Buses Active            : %d\n" +
                "Traffic Level           : %s\n" +
                "Average Travel Time     : %.0f minutes\n" +
                "Public Transport Load   : %.1f%%\n" +
                "Status                  : %s",
                busesActive, trafficLevel, averageTravelTime, publicTransportLoad, status);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "TRANSPORT SYSTEM\n" +
                "-".repeat(40) + "\n" +
                "Average Traffic Level   : %s\n" +
                "Average Travel Time     : %.0f minutes\n" +
                "Public Transport Load   : %.1f%%\n" +
                "Status                  : %s\n",
                trafficLevel, averageTravelTime, publicTransportLoad, status);
    }
    
    @Override
    public boolean isCritical() {
        return status.equals("CRITICAL") || trafficLevel.equals("CRITICAL");
    }
    
    @Override
    public void reset() {
        this.currentLoad = 0;
        this.trafficLevel = "LOW";
        this.averageTravelTime = 20.0;
        this.publicTransportLoad = 0;
        this.status = "STABLE";
    }
    
    public int getBusesActive() {
        return busesActive;
    }
    
    public String getTrafficLevel() {
        return trafficLevel;
    }
    
    public double getAverageTravelTime() {
        return averageTravelTime;
    }
    
    public double getPublicTransportLoad() {
        return publicTransportLoad;
    }
}