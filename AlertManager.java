package managers;

import systems.*;
import entities.*;
import java.util.*;

public class AlertManager {
    private List<String> alerts;
    private List<String> suggestions;
    
    public AlertManager() {
        this.alerts = new ArrayList<>();
        this.suggestions = new ArrayList<>();
    }
    
    public void checkSystems(City city) {
        alerts.clear();
        suggestions.clear();
        
        checkTransportSystem(city.getTransport());
        checkEnergySystem(city.getEnergy());
        checkFinanceSystem(city.getFinance());
        checkWaterSystem(city.getWater());
    }
    
    private void checkTransportSystem(TransportSystem transport) {
        double loadPercent = transport.getLoadPercentage();
        
        if (transport.getAverageTravelTime() > 50) {
            alerts.add("[WARNING] Traffic congestion exceeded safe threshold.\n" +
                      "          Average travel time above 50 minutes.");
            suggestions.add("[SUGGESTION] Increase number of buses\n" +
                           "             or improve road management.");
        }
        
        if (transport.isCritical()) {
            alerts.add("[CRITICAL] Transport system is critically overloaded.");
            suggestions.add("[SUGGESTION] Implement emergency traffic diversion.");
        }
    }
    
    private void checkEnergySystem(EnergySystem energy) {
        double loadPercent = (energy.getPowerUsage() / energy.getPowerProduction()) * 100;
        
        if (loadPercent > 80) {
            alerts.add("[NOTICE] Power usage above 80%.");
            suggestions.add("[SUGGESTION] Encourage off-peak electricity usage.");
        }
        
        if (energy.isCritical()) {
            alerts.add("[CRITICAL] Energy system critical.");
            suggestions.add("[SUGGESTION] Initiate immediate load reduction.");
        }
    }
    
    private void checkFinanceSystem(FinanceSystem finance) {
        if (finance.getNetDailyChange() < 0) {
            alerts.add("[INFO] City budget deficit detected today.");
            suggestions.add("[SUGGESTION] Reduce operational costs or increase taxes.");
        }
        
        if (finance.isCritical()) {
            alerts.add("[CRITICAL] City facing severe financial crisis.");
            suggestions.add("[SUGGESTION] Implement emergency budget cuts.");
        }
    }
    
    private void checkWaterSystem(WaterSystem water) {
        if (water.getLoadPercentage() > 85) {
            alerts.add("[WARNING] Water consumption near capacity.");
            suggestions.add("[SUGGESTION] Promote water conservation.");
        }
    }
    
    public void displayAlerts() {
        if (alerts.isEmpty() && suggestions.isEmpty()) {
            System.out.println("✅ All systems operating normally. No alerts.");
            return;
        }
        
        System.out.println("\n" + "*".repeat(50));
        System.out.println("SYSTEM ALERTS");
        System.out.println("*".repeat(50));
        
        for (int i = 0; i < alerts.size(); i++) {
            System.out.println("\n" + alerts.get(i));
            System.out.println();
            System.out.println(suggestions.get(i));
        }
        
        System.out.println("\n" + "*".repeat(50));
    }
    
    public boolean hasAlerts() {
        return !alerts.isEmpty();
    }
}