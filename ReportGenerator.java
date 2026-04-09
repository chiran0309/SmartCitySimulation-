package managers;

import systems.*;
import entities.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class ReportGenerator {
    private final City city;
    private final DateTimeFormatter dateFormatter;
    
    public ReportGenerator(City city) {
        this.city = city;
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    public void generateDetailedReport() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("SMART CITY SIMULATION REPORT");
        System.out.println("=".repeat(50));
        System.out.printf("\nCity Name        : %s\n", city.getCityName());
        System.out.printf("Simulation Day   : %d\n", city.getCurrentDay());
        System.out.printf("Generated At     : %s\n", LocalDateTime.now().format(dateFormatter));
        System.out.println("\n" + "-".repeat(50));
        System.out.println(city.getPopulation().getReport());
        System.out.println("-".repeat(50));
        System.out.println(city.getTransport().getReport());
        System.out.println("-".repeat(50));
        System.out.println(city.getEnergy().getReport());
        System.out.println("-".repeat(50));
        System.out.println(city.getWater().getReport());
        System.out.println("-".repeat(50));
        System.out.println(city.getFinance().getReport());
        System.out.println("-".repeat(50));
        
        // 🆕 NEW: Show Happiness Report
        generateHappinessReport();
        System.out.println("-".repeat(50));
        
        // 🆕 NEW: Show System Dependencies
        generateSystemDependenciesReport();
        System.out.println("-".repeat(50));
        
        generateGovernanceInsights();
        generateHealthScores();
        
        System.out.println("=".repeat(50));
        System.out.println("End of Report");
        System.out.println("=".repeat(50));
    }
    
    // 🆕 NEW METHOD: Happiness Report
    private void generateHappinessReport() {
        System.out.println("\nCITIZEN HAPPINESS & MOOD");
        System.out.println("-".repeat(50));
        
        HappinessSystem happiness = city.getHappiness();
        double happinessValue = happiness.getHappiness();
        double happinessChange = happiness.getHappinessChange();
        String moodStatus = happiness.getMoodStatus();
        
        String moodEmoji = switch(moodStatus) {
            case "HAPPY" -> "😊";
            case "CONTENT" -> "😐";
            case "STRESSED" -> "😟";
            case "CRITICAL" -> "😡";
            default -> "❓";
        };
        
        System.out.printf("Happiness Level : %.0f/100 %s\n", happinessValue, moodEmoji);
        System.out.printf("Mood Status     : %s\n", moodStatus);
        System.out.printf("Change Today    : %s%.1f (%s)\n", 
            happinessChange > 0 ? "+" : "", 
            happinessChange,
            happinessChange > 0 ? "📈" : "📉");
        
        // Show what affects happiness
        System.out.println("\nHappiness Affected By:");
        double trafficLoad = city.getTransport().getLoadPercentage();
        double energyLoad = city.getEnergy().getLoadPercentage();
        double waterLoad = city.getWater().getLoadPercentage();
        long budget = city.getFinance().getCityBudget();
        
        if (trafficLoad > 80) {
            System.out.println("  ⚠️  High traffic congestion (negative impact)");
        } else if (trafficLoad > 60) {
            System.out.println("  🟡 Moderate traffic (slight negative impact)");
        } else {
            System.out.println("  ✅ Good traffic conditions");
        }
        
        if (energyLoad > 95) {
            System.out.println("  ⚠️  Critical power grid (major negative impact)");
        } else if (energyLoad > 80) {
            System.out.println("  🟡 High energy demand (negative impact)");
        } else {
            System.out.println("  ✅ Stable power grid");
        }
        
        if (waterLoad > 90) {
            System.out.println("  ⚠️  Water shortage affecting health");
        } else {
            System.out.println("  ✅ Adequate water supply");
        }
        
        if (budget < 5000000) {
            System.out.println("  ⚠️  Budget crisis affecting services");
        } else if (budget < 10000000) {
            System.out.println("  🟡 Budget concerns");
        } else {
            System.out.println("  ✅ Healthy budget");
        }
    }
    
    // 🆕 NEW METHOD: System Dependencies Report
    private void generateSystemDependenciesReport() {
        System.out.println("\nSYSTEM INTERCONNECTIONS & IMPACTS");
        System.out.println("-".repeat(50));
        
        double trafficLoad = city.getTransport().getLoadPercentage();
        double energyLoad = city.getEnergy().getLoadPercentage();
        double waterLoad = city.getWater().getLoadPercentage();
        
        System.out.println("System Dependencies Active:");
        
        // Energy → Transport
        if (energyLoad > 95) {
            System.out.println("  ⚡→🚗 Power shortage affecting traffic signals");
            System.out.println("     Impact: +15% congestion due to signal failures");
        }
        
        // Transport → Economy
        if (trafficLoad > 85) {
            System.out.println("  🚗→💰 Heavy congestion affecting productivity");
            System.out.println("     Impact: -8% tax income due to lost productivity");
        }
        
        // Energy → Water
        if (energyLoad > 90) {
            System.out.println("  ⚡→💧 Power shortage affecting water pumps");
            System.out.println("     Impact: +5% water system load");
        }
        
        // Transport → Environment
        if (trafficLoad > 75) {
            System.out.println("  🚗→⚡ Traffic congestion increasing air pollution");
            System.out.println("     Impact: +3% energy demand from pollution control");
        }
        
        // Water → Health
        if (waterLoad > 85) {
            System.out.println("  💧→👥 Water shortage affecting public health");
            System.out.println("     Impact: -2% workforce productivity");
        }
        
        // Finance → All Systems
        long budget = city.getFinance().getCityBudget();
        if (budget < 5000000) {
            System.out.println("  💰→🔧 Budget crisis: maintenance deferred");
            System.out.println("     Impact: All systems -5% efficiency");
        }
        
        if (trafficLoad < 60 && energyLoad < 80 && waterLoad < 80 && budget > 10000000) {
            System.out.println("  ✅ All systems operating independently");
            System.out.println("     No major interdependency impacts detected");
        }
    }
    
    private void generateGovernanceInsights() {
        System.out.println("\nGOVERNANCE INSIGHTS");
        System.out.println("-".repeat(50));
        
        List<String> insights = new ArrayList<>();
        
        // Traffic insights
        if (city.getTransport().getAverageTravelTime() > 45) {
            insights.add("• Increase public transport capacity");
            insights.add("• Encourage remote work during peak hours");
        }
        
        // Energy insights
        if ((city.getEnergy().getPowerUsage() / city.getEnergy().getPowerProduction()) * 100 > 75) {
            insights.add("• Initiate electricity conservation campaign");
        }
        
        // Finance insights
        if (city.getFinance().getNetDailyChange() < 0) {
            insights.add("• Review transport maintenance costs");
            insights.add("• Optimize waste management expenses");
        }
        
        // Water insights
        if (city.getWater().getLoadPercentage() > 70) {
            insights.add("• Launch water conservation initiatives");
        }
        
        // Happiness insights
        double happiness = city.getHappiness().getHappiness();
        if (happiness < 50) {
            insights.add("• Citizen satisfaction critical - improve services urgently");
        } else if (happiness < 70) {
            insights.add("• Increase investment in public services to boost morale");
        }
        
        if (insights.isEmpty()) {
            insights.add("• Continue current operational policies");
            insights.add("• Monitor systems for future optimization");
        }
        
        insights.forEach(System.out::println);
    }
    
    private void generateHealthScores() {
        System.out.println("\nOVERALL CITY HEALTH");
        System.out.println("-".repeat(50));
        
        TransportSystem transport = city.getTransport();
        EnergySystem energy = city.getEnergy();
        FinanceSystem finance = city.getFinance();
        HappinessSystem happiness = city.getHappiness();
        
        double transportScore = Math.max(0, 100 - (transport.getLoadPercentage() * 0.5));
        double energyScore = Math.max(0, 100 - (energy.getLoadPercentage() * 0.3));
        double financeScore = Math.max(0, 100 - (finance.getLoadPercentage() * 0.2));
        double happinessScore = happiness.getHappiness();
        
        System.out.printf("Transport Score : %.0f\n", transportScore);
        System.out.printf("Energy Score    : %.0f\n", energyScore);
        System.out.printf("Finance Score   : %.0f\n", financeScore);
        System.out.printf("Happiness Score : %.0f\n", happinessScore);
        
        double healthIndex = city.getOverallHealthIndex();
        String cityStatus = healthIndex > 80 ? "🟢 EXCELLENT" : 
                           healthIndex > 65 ? "🟡 STABLE" : 
                           healthIndex > 50 ? "🟠 WARNING" : "🔴 CRITICAL";
        
        System.out.printf("\nHealth Index    : %.0f / 100\n", healthIndex);
        System.out.printf("City Status     : %s", cityStatus);
        
        if (healthIndex <= 65) {
            System.out.print(" (Immediate action recommended)");
        }
        System.out.println();
    }
}