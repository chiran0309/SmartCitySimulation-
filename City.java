package entities;

import systems.*;
import exceptions.*;
import java.util.*;

public class City {
    private final String cityName;
    private int currentDay;
    private Population population;
    private TransportSystem transport;
    private EnergySystem energy;
    private WaterSystem water;
    private FinanceSystem finance;
    private HappinessSystem happiness;
    
    private List<String> dailyHistory;
    private List<Double> healthHistory;
    private List<Long> budgetHistory;
    private List<Integer> populationHistory;
    private Random random;
    
    public City(String cityName, int initialPopulation, int initialBuses,
                double energyProduction, double waterSupply, long initialBudget)
            throws InvalidParameterException, InvalidCityException {
        
        if (cityName == null || cityName.isEmpty()) {
            throw new InvalidCityException("City name cannot be null or empty");
        }
        
        this.cityName = cityName;
        this.currentDay = 1;
        
        this.dailyHistory = new ArrayList<>();
        this.healthHistory = new ArrayList<>();
        this.budgetHistory = new ArrayList<>();
        this.populationHistory = new ArrayList<>();
        this.random = new Random();
        
        try {
            this.population = new Population(initialPopulation);
            this.transport = new TransportSystem(initialBuses);
            this.energy = new EnergySystem(energyProduction);
            this.water = new WaterSystem(waterSupply);
            this.finance = new FinanceSystem(initialBudget);
            this.happiness = new HappinessSystem(85);
        } catch (InvalidParameterException e) {
            throw new InvalidCityException("Failed to initialize city: " + e.getMessage());
        }
        
        recordDay("City initialized");
        recordMetrics();
    }
    
    public void recordDay(String event) {
        String record = String.format("Day %d: %s", currentDay, event);
        dailyHistory.add(record);
    }
    
    public void recordMetrics() {
        healthHistory.add(getOverallHealthIndex());
        budgetHistory.add(finance.getCityBudget());
        populationHistory.add(population.getTotalPopulation());
    }
    
    public void simulateHappiness() {
        happiness.updateHappiness(this);
    }
    
    public void applySystemDependencies() throws InvalidParameterException {
        SystemDependencies.applyDependencies(this);
    }
    
    public void nextDay() {
        this.currentDay++;
        simulateDailyGrowth();
    }
    
    private void simulateDailyGrowth() {
        double happinessFactor = happiness.getHappiness() / 100.0;
        int populationGrowth = Math.max(1, (int) (population.getTotalPopulation() * 0.001 * happinessFactor));
        
        recordDay(String.format("Population grew by %,d citizens", populationGrowth));
    }
    
    public void displayCityStatusEnhanced() {
        System.out.println("\n" + "╔" + "═".repeat(60) + "╗");
        System.out.println("║" + " ".repeat(10) + "🏙  SMART CITY DASHBOARD  🏙" + " ".repeat(10) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        
        double health = getOverallHealthIndex();
        String healthStatus = health > 80 ? "🟢 EXCELLENT" : 
                             health > 65 ? "🟡 STABLE" : 
                             health > 50 ? "🟠 WARNING" : "🔴 CRITICAL";
        
        System.out.printf("%-30s : %s (Day %d)\n", "City", cityName, currentDay);
        System.out.printf("%-30s : %,d 👥\n", "Population", population.getTotalPopulation());
        System.out.printf("%-30s : %,d 💼\n", "Active Workforce", population.getActiveWorkforce());
        System.out.printf("%-30s : %s\n", "Overall Status", healthStatus);
        System.out.printf("%-30s : ৳%,d\n", "City Budget", finance.getCityBudget());
        
        double happinessValue = happiness.getHappiness();
        String happinessEmoji = happinessValue > 75 ? "😊" : 
                               happinessValue > 50 ? "😐" : 
                               happinessValue > 25 ? "😟" : "😡";
        System.out.printf("%-30s : %s (%.0f/100)\n", "Citizen Happiness", happinessEmoji, happinessValue);
        
        System.out.println("\n" + "─".repeat(60));
        System.out.println("DETAILED SYSTEMS:");
        System.out.println("─".repeat(60));
        
        String transportEmoji = transport.getTrafficLevel().equals("LOW") ? "🟢" :
                               transport.getTrafficLevel().equals("MEDIUM") ? "🟡" :
                               transport.getTrafficLevel().equals("HIGH") ? "🟠" : "🔴";
        System.out.printf("🚗 TRANSPORT    %s | Buses: %d | Traffic: %s | Travel Time: %.0f min\n",
            transportEmoji, transport.getBusesActive(), transport.getTrafficLevel(), 
            transport.getAverageTravelTime());
        
        double energyUsagePercent = (energy.getPowerUsage() / energy.getPowerProduction()) * 100;
        String energyEmoji = energyUsagePercent < 80 ? "🟢" : energyUsagePercent < 95 ? "🟡" : "🔴";
        System.out.printf("⚡ ENERGY      %s | Usage: %.1f%% | Status: %s\n",
            energyEmoji, energyUsagePercent, energy.getGridStatus());
        
        double waterUsagePercent = (water.getWaterConsumption() / water.getWaterSupplyCapacity()) * 100;
        String waterEmoji = waterUsagePercent < 80 ? "🟢" : "🟠";
        System.out.printf("💧 WATER       %s | Consumption: %.1f%% | Status: %s\n",
            waterEmoji, waterUsagePercent, water.getStatus());
        
        String financeEmoji = finance.getNetDailyChange() > 0 ? "📈" : "📉";
        System.out.printf("💰 FINANCE     %s | Budget: ৳%,d | Daily Change: %s৳%,d\n",
            financeEmoji, finance.getCityBudget(),
            finance.getNetDailyChange() < 0 ? "-" : "+",
            Math.abs(finance.getNetDailyChange()));
        
        System.out.println("═".repeat(60));
    }
    
    public void displayTrends() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📊 CITY TRENDS (Last 10 Days)");
        System.out.println("═".repeat(60));
        
        System.out.println("\n📈 HEALTH INDEX TREND:");
        int startIdx = Math.max(0, healthHistory.size() - 10);
        for (int i = startIdx; i < healthHistory.size(); i++) {
            double health = healthHistory.get(i);
            int bars = (int) (health / 10);
            String healthEmoji = health > 80 ? "🟢" : health > 65 ? "🟡" : health > 50 ? "🟠" : "🔴";
            System.out.printf("Day %2d: %s %s %.0f/100\n", 
                i + 1, 
                healthEmoji,
                "█".repeat(bars) + "░".repeat(10 - bars),
                health);
        }
        
        System.out.println("\n💰 BUDGET TREND:");
        if (!budgetHistory.isEmpty()) {
            for (int i = startIdx; i < budgetHistory.size(); i++) {
                long budget = budgetHistory.get(i);
                String status = budget < 5000000 ? "🔴 CRITICAL" : 
                               budget < 10000000 ? "🟡 WARNING" : "🟢 GOOD";
                System.out.printf("Day %2d: ৳%,d %s\n", i + 1, budget, status);
            }
        }
        
        System.out.println("\n👥 POPULATION TREND:");
        if (!populationHistory.isEmpty()) {
            for (int i = startIdx; i < populationHistory.size(); i++) {
                int pop = populationHistory.get(i);
                System.out.printf("Day %2d: %,d citizens\n", i + 1, pop);
            }
        }
        
        System.out.println("═".repeat(60));
    }
    
    public void displayHistory() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📜 CITY EVENT LOG");
        System.out.println("═".repeat(60));
        
        if (dailyHistory.isEmpty()) {
            System.out.println("No events recorded yet.");
        } else {
            int startIndex = Math.max(0, dailyHistory.size() - 15);
            for (int i = startIndex; i < dailyHistory.size(); i++) {
                System.out.println(dailyHistory.get(i));
            }
        }
        
        System.out.println("═".repeat(60));
    }
    
    public void displayCityStatus() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        SMART CITY SIMULATION v2.0");
        System.out.println("=".repeat(50));
        System.out.printf("\nCity Name       : %s\n", cityName);
        System.out.printf("Current Day     : %d\n", currentDay);
        System.out.printf("Population      : %,d\n", population.getTotalPopulation());
        System.out.printf("Workforce       : %,d\n", population.getActiveWorkforce());
        System.out.println("\n" + "-".repeat(50));
        System.out.println("TRANSPORT SYSTEM");
        System.out.println("-".repeat(50));
        System.out.println(transport.getDetailedStatus());
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ENERGY SYSTEM");
        System.out.println("-".repeat(50));
        System.out.println(energy.getDetailedStatus());
        System.out.println("\n" + "-".repeat(50));
        System.out.println("WATER SYSTEM");
        System.out.println("-".repeat(50));
        System.out.println(water.getDetailedStatus());
        System.out.println("\n" + "-".repeat(50));
        System.out.println("CITY FINANCE");
        System.out.println("-".repeat(50));
        System.out.println(finance.getDetailedStatus());
    }
    
    public String getCityName() { return cityName; }
    public int getCurrentDay() { return currentDay; }
    public Population getPopulation() { return population; }
    public TransportSystem getTransport() { return transport; }
    public EnergySystem getEnergy() { return energy; }
    public WaterSystem getWater() { return water; }
    public FinanceSystem getFinance() { return finance; }
    public HappinessSystem getHappiness() { return happiness; }
    public List<String> getDailyHistory() { return new ArrayList<>(dailyHistory); }
    
    public double getOverallHealthIndex() {
        double transportScore = Math.max(0, 100 - (transport.getLoadPercentage() * 0.5));
        double energyScore = Math.max(0, 100 - (energy.getLoadPercentage() * 0.3));
        double financeScore = Math.max(0, 100 - (finance.getLoadPercentage() * 0.2));
        double happinessScore = happiness.getHappiness() * 0.5;
        
        return (transportScore + energyScore + financeScore + happinessScore) / 4.0;
    }
    
}