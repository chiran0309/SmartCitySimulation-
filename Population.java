package systems;

import exceptions.*;
import java.util.*;

public class Population extends CitySystem implements SimulationSystem {
    private int totalPopulation;
    private int activeWorkforce;
    private int publicTransportUsers;
    private Random random;
    
    public Population(int initialPopulation) 
            throws InvalidParameterException, InvalidCityException {
        super("Population System", initialPopulation * 0.8);
        
        if (initialPopulation <= 0) {
            throw new InvalidCityException("Population must be positive");
        }
        
        this.totalPopulation = initialPopulation;
        this.activeWorkforce = (int) (initialPopulation * 0.568);
        this.publicTransportUsers = (int) (activeWorkforce * 0.694);
        this.random = new Random();
    }
    
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException {
        int travelingCitizens = (int) (publicTransportUsers * (0.8 + random.nextDouble() * 0.2));
        setCurrentLoad(travelingCitizens);
        
        if (getLoadPercentage() > 95) {
            throw new SystemOverloadException("Population System", getLoadPercentage());
        }
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        try {
            System.out.println("\n📊 Population System");
            System.out.println("-".repeat(40));
            update();
            System.out.println("Citizens traveled to workplaces.");
            System.out.println("Public transport demand increased.");
        } catch (SystemOverloadException e) {
            System.err.println("⚠️  " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void handleOverload() {
        System.out.println("⚠️  Population overload detected. Suggesting remote work options.");
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "Total Population         : %,d\n" +
                "Active Workforce         : %,d\n" +
                "Public Transport Users   : %,d\n" +
                "Status                   : %s",
                totalPopulation, activeWorkforce, publicTransportUsers, status);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "POPULATION SUMMARY\n" +
                "-".repeat(40) + "\n" +
                "Total Population        : %,d\n" +
                "Active Workforce        : %,d\n" +
                "Public Transport Users  : %,d\n",
                totalPopulation, activeWorkforce, publicTransportUsers);
    }
    
    @Override
    public boolean isCritical() {
        return status.equals("CRITICAL");
    }
    
    @Override
    public void reset() {
        this.currentLoad = 0;
        this.status = "STABLE";
    }
    
    public int getTotalPopulation() {
        return totalPopulation;
    }
    
    public int getActiveWorkforce() {
        return activeWorkforce;
    }
    
    public int getPublicTransportUsers() {
        return publicTransportUsers;
    }
}