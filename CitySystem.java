package systems;

import exceptions.*;

public abstract class CitySystem {
    protected String systemName;
    protected double currentLoad;
    protected double maxCapacity;
    protected String status;
    
    public CitySystem(String systemName, double maxCapacity) 
            throws InvalidParameterException {
        if (systemName == null || systemName.isEmpty()) {
            throw new InvalidParameterException("systemName", "Cannot be null or empty");
        }
        if (maxCapacity <= 0) {
            throw new InvalidParameterException("maxCapacity", "Must be greater than 0");
        }
        
        this.systemName = systemName;
        this.maxCapacity = maxCapacity;
        this.currentLoad = 0;
        this.status = "STABLE";
    }
    
    public abstract void update() throws SystemOverloadException, InvalidParameterException, InsufficientFundsException;
    public abstract String getDetailedStatus();
    public abstract void handleOverload();
    
    public final void simulateDayActivity() throws SystemOverloadException {
        try {
            update();
            evaluateStatus();
        } catch (SystemOverloadException e) {
            handleOverload();
            throw e;
        } catch (InvalidParameterException | InsufficientFundsException e) {
            // Handle other exceptions if needed
        }
    }
    
    protected void evaluateStatus() {
        double loadPercentage = (currentLoad / maxCapacity) * 100;
        if (loadPercentage < 60) {
            this.status = "STABLE";
        } else if (loadPercentage < 85) {
            this.status = "WARNING";
        } else {
            this.status = "CRITICAL";
        }
    }
    
    public String getSystemName() {
        return systemName;
    }
    
    public double getCurrentLoad() {
        return currentLoad;
    }
    
    public double getMaxCapacity() {
        return maxCapacity;
    }
    
    public double getLoadPercentage() {
        return (currentLoad / maxCapacity) * 100;
    }
    
    public String getStatus() {
        return status;
    }
    
    protected void setCurrentLoad(double load) throws InvalidParameterException {
        if (load < 0 || load > maxCapacity * 1.2) {
            throw new InvalidParameterException("currentLoad", 
                    "Must be between 0 and " + (maxCapacity * 1.2));
        }
        this.currentLoad = load;
    }
    
    @Override
    public String toString() {
        return String.format("%s [Load: %.1f/%,.1f (%.1f%%) - %s]",
                systemName, currentLoad, maxCapacity, getLoadPercentage(), status);
    }
}