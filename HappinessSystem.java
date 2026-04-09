package systems;

import entities.City;
import exceptions.*;
import java.util.*;

public class HappinessSystem extends CitySystem implements SimulationSystem {
    private double citizenHappiness; // 0-100
    private double lastHappiness;
    private String moodStatus;
    private Random random;
    private List<String> happinessHistory;
    private int consecutiveLowDays;
    
    public HappinessSystem(double initialHappiness) 
            throws InvalidParameterException {
        super("Happiness System", 100);
        
        if (initialHappiness < 0 || initialHappiness > 100) {
            throw new InvalidParameterException("initialHappiness", 
                    "Must be between 0 and 100");
        }
        
        this.citizenHappiness = initialHappiness;
        this.lastHappiness = initialHappiness;
        this.moodStatus = "HAPPY";
        this.random = new Random();
        this.happinessHistory = new ArrayList<>();
        this.consecutiveLowDays = 0;
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        System.out.println("\n😊 Happiness System");
        System.out.println("-".repeat(40));
        System.out.printf("Citizen Happiness: %.0f/100 (%s)\n", citizenHappiness, moodStatus);
        System.out.printf("Change Today: %s%.1f\n", 
            getHappinessChange() > 0 ? "+" : "", 
            getHappinessChange());
    }
    
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException {
        setCurrentLoad(100 - citizenHappiness);
        evaluateStatus();
    }
    
    public void updateHappiness(City city) {
        lastHappiness = citizenHappiness;
        double happinessAdjustment = 0;
        
        // TRAFFIC IMPACT: -1 to -5 points
        double trafficPercent = city.getTransport().getLoadPercentage();
        if (trafficPercent > 90) {
            happinessAdjustment -= 5;
        } else if (trafficPercent > 75) {
            happinessAdjustment -= 3;
        } else if (trafficPercent > 60) {
            happinessAdjustment -= 1;
        }
        
        // ENERGY IMPACT: -1 to -5 points
        double energyPercent = city.getEnergy().getLoadPercentage();
        if (energyPercent > 95) {
            happinessAdjustment -= 5;
        } else if (energyPercent > 80) {
            happinessAdjustment -= 2;
        }
        
        // FINANCE IMPACT: -1 to -3 points
        long budget = city.getFinance().getCityBudget();
        if (budget < 5000000) {
            happinessAdjustment -= 3;
        } else if (budget < 10000000) {
            happinessAdjustment -= 1;
        } else if (budget > 20000000) {
            happinessAdjustment += 1;
        }
        
        // WATER IMPACT: -1 to -3 points
        double waterPercent = city.getWater().getLoadPercentage();
        if (waterPercent > 90) {
            happinessAdjustment -= 3;
        } else if (waterPercent > 75) {
            happinessAdjustment -= 1;
        }
        
        // NATURAL RECOVERY: +1 point
        if (happinessAdjustment >= 0) {
            happinessAdjustment += 1;
        }
        
        // ADD RANDOMNESS: ±1-2 points
        double randomVariation = (random.nextDouble() - 0.5) * 4;
        happinessAdjustment += randomVariation;
        
        // Apply adjustment
        citizenHappiness += happinessAdjustment;
        
        // Clamp between 0-100
        citizenHappiness = Math.max(0, Math.min(100, citizenHappiness));
        
        // Update mood and track
        updateMoodStatus();
        trackConsecutiveLowDays();
        recordHistory();
    }
    
    private void trackConsecutiveLowDays() {
        if (citizenHappiness < 40) {
            consecutiveLowDays++;
        } else {
            consecutiveLowDays = 0;
        }
        
        if (consecutiveLowDays >= 3) {
            System.out.println("   ⚠️  CRITICAL: Citizens unhappy for 3+ days!");
            System.out.println("   📉 Population decline expected if not addressed");
        }
    }
    
    private void recordHistory() {
        String record = String.format("%.0f", citizenHappiness);
        happinessHistory.add(record);
    }
    
    private void updateMoodStatus() {
        if (citizenHappiness >= 80) {
            moodStatus = "HAPPY";
        } else if (citizenHappiness >= 60) {
            moodStatus = "CONTENT";
        } else if (citizenHappiness >= 40) {
            moodStatus = "STRESSED";
        } else {
            moodStatus = "CRITICAL";
        }
        
        try {
            setCurrentLoad(100 - citizenHappiness);
        } catch (InvalidParameterException e) {
            // Handle silently
        }
    }
    
    public void displayStatus() {
        String moodEmoji = switch(moodStatus) {
            case "HAPPY" -> "😊";
            case "CONTENT" -> "😐";
            case "STRESSED" -> "😟";
            case "CRITICAL" -> "😡";
            default -> "❓";
        };
        
        String happinessBar = "█".repeat((int)(citizenHappiness / 10)) + 
                             "░".repeat(10 - (int)(citizenHappiness / 10));
        
        System.out.printf("Citizen Happiness : %s %.0f/100 %s\n", 
            happinessBar, citizenHappiness, moodEmoji);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "CITIZEN HAPPINESS\n" +
                "-".repeat(40) + "\n" +
                "Happiness Level         : %.0f/100\n" +
                "Mood Status             : %s\n" +
                "Change Today            : %s%.1f\n",
                citizenHappiness, moodStatus,
                getHappinessChange() > 0 ? "+" : "",
                getHappinessChange());
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "Happiness               : %.0f/100\n" +
                "Mood Status             : %s\n" +
                "Consecutive Low Days    : %d\n" +
                "Status                  : %s",
                citizenHappiness, moodStatus, consecutiveLowDays, status);
    }
    
    @Override
    public boolean isCritical() {
        return moodStatus.equals("CRITICAL") || consecutiveLowDays >= 3;
    }
    
    @Override
    public void reset() {
        this.currentLoad = 0;
        this.status = "STABLE";
        this.consecutiveLowDays = 0;
    }
    
    @Override
    public void handleOverload() {
        System.out.println("🚨 CRITICAL: Citizens extremely unhappy!");
        System.out.println("📉 Immediate action required to prevent population exodus!");
    }
    
    public double getHappiness() { return citizenHappiness; }
    public double getHappinessChange() { return citizenHappiness - lastHappiness; }
    public String getMoodStatus() { return moodStatus; }
    public int getConsecutiveLowDays() { return consecutiveLowDays; }
    public List<String> getHappinessHistory() { return new ArrayList<>(happinessHistory); }
}