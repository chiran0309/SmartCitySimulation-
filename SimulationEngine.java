package managers;

import systems.*;
import entities.*;
import exceptions.*;
import java.util.Random;

public class SimulationEngine {
    private City city;
    private AlertManager alertManager;
    private ReportGenerator reportGenerator;
    private String currentScenario;
    private boolean isRunning;
    
    public SimulationEngine(City city) {
        this.city = city;
        this.alertManager = new AlertManager();
        this.reportGenerator = new ReportGenerator(city);
        this.currentScenario = "Normal Day";
        this.isRunning = true;
    }
    
    public void runOneDay() throws SimulationException {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + String.format(" SIMULATING DAY %d ", city.getCurrentDay() + 1).replaceAll("(.)", " $1").substring(1) + " ".repeat(30) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝\n");
        
        try {
            // Check for random events
            RandomEvent event = RandomEvent.generateEvent();
            if (event != null) {
                System.out.println(event.getDescription());
                event.apply(city);
                System.out.println();
            }
            
            // Run normal simulation
            runSystemsSimulation();
            
            // Apply system dependencies
            System.out.println();
            city.applySystemDependencies();
            
            // Update happiness
            System.out.println();
            city.simulateHappiness();
            
            // Record metrics
            city.recordMetrics();
            
            // Check alerts
            alertManager.checkSystems(city);
            
            // Increment day
            city.nextDay();
            
            System.out.println("\n✅ Day " + city.getCurrentDay() + " simulation completed successfully!");
            
        } catch (SystemOverloadException e) {
            System.err.println("\n❌ System Overload: " + e.getMessage());
            throw e;
        } catch (InsufficientFundsException e) {
            System.err.println("\n❌ Financial Crisis: " + e.getMessage());
            throw e;
        } catch (SimulationException e) {
            System.err.println("\n❌ Simulation Error: " + e.getMessage());
            throw e;
        }
    }
    
    private void runSystemsSimulation() throws SimulationException {
        System.out.println("📊 Running system simulations...\n");
        city.getPopulation().simulateOneDay();
        city.getTransport().simulateOneDay();
        city.getEnergy().simulateOneDay();
        city.getWater().simulateOneDay();
        city.getFinance().simulateOneDay();
    }
    
    public void setScenario(String scenario) throws InvalidParameterException {
        switch (scenario.toLowerCase()) {
            case "normal day":
            case "festival day":
            case "power crisis":
            case "transport strike":
                this.currentScenario = scenario;
                System.out.println("\n🎯 Scenario Selected: " + scenario);
                applyScenarioEffects();
                break;
            default:
                throw new InvalidParameterException("scenario", 
                        "Unknown scenario: " + scenario);
        }
    }
    
    private void applyScenarioEffects() {
        try {
            switch (currentScenario.toLowerCase()) {
                case "festival day":
                    System.out.println("🎉 Population movement increased.");
                    System.out.println("🚗 Transport demand increased by 25%.");
                    System.out.println("⚡ Energy consumption increased by 12%.");
                    city.recordDay("Festival Day scenario activated");
                    break;
                    
                case "power crisis":
                    System.out.println("⚡ Power production reduced by 40%.");
                    System.out.println("🔴 Energy rationing implemented.");
                    city.recordDay("Power Crisis scenario activated");
                    break;
                    
                case "transport strike":
                    System.out.println("🚗 Public buses unavailable.");
                    System.out.println("🔴 Traffic congestion expected.");
                    city.recordDay("Transport Strike scenario activated");
                    break;
                    
                case "normal day":
                default:
                    System.out.println("✅ All systems operating normally.");
                    city.recordDay("Normal Day scenario activated");
            }
        } catch (Exception e) {
            System.err.println("⚠️  Error applying scenario: " + e.getMessage());
        }
    }
    
    public void displayAlerts() {
        alertManager.displayAlerts();
    }
    
    public void displayReport() {
        reportGenerator.generateDetailedReport();
    }
    
    public void displayCityStatusEnhanced() {
        city.displayCityStatusEnhanced();
    }
    
    public void displayTrends() {
        city.displayTrends();
    }
    
    public void displayHistory() {
        city.displayHistory();
    }
    
    public void displayCityStatus() {
        city.displayCityStatus();
        System.out.println("\n" + "-".repeat(50));
        System.out.println("CITY HEALTH INDEX");
        System.out.println("-".repeat(50));
        
        double healthIndex = city.getOverallHealthIndex();
        String status = healthIndex > 75 ? "🟢 EXCELLENT" :
                       healthIndex > 65 ? "🟡 STABLE" :
                       healthIndex > 50 ? "🟠 WARNING (Needs Attention)" : "🔴 CRITICAL";
        
        System.out.printf("Overall Health Index : %.0f / 100\n", healthIndex);
        System.out.printf("City Status          : %s\n", status);
    }
    
    public City getCity() { return city; }
    public String getCurrentScenario() { return currentScenario; }
    public boolean isRunning() { return isRunning; }
    public void stop() { this.isRunning = false; }
}