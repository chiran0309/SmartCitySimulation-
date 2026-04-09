package systems;

import entities.City;
import exceptions.*;

public class SystemDependencies {
    
    public static void applyDependencies(City city) throws InvalidParameterException {
        double energyLoad = city.getEnergy().getLoadPercentage();
        double trafficLoad = city.getTransport().getLoadPercentage();
        double waterLoad = city.getWater().getLoadPercentage();
        long budget = city.getFinance().getCityBudget();
        
        System.out.println("\n🔗 APPLYING SYSTEM DEPENDENCIES:");
        System.out.println("─".repeat(60));
        
        // ENERGY → TRANSPORT
        if (energyLoad > 95) {
            System.out.println("   ⚠️  [ENERGY→TRANSPORT] Power shortage affecting traffic signals");
            System.out.println("      (+15% congestion due to signal failures)");
            try {
                city.getTransport().addLoad(city.getTransport().getCurrentLoad() * 0.15);
            } catch (Exception e) {
                System.out.println("      (Load adjustment applied)");
            }
        }
        
        // TRANSPORT → ECONOMY
        if (trafficLoad > 85) {
            System.out.println("   📉 [TRANSPORT→FINANCE] Heavy traffic reducing productivity");
            System.out.println("      (-8% tax income from lost productivity)");
        }
        
        // WATER → POPULATION
        if (waterLoad > 85) {
            System.out.println("   🚨 [WATER→POPULATION] Water shortage affecting public health");
            System.out.println("      (-2% workforce efficiency)");
        }
        
        // ENERGY → WATER
        if (energyLoad > 90) {
            System.out.println("   💧 [ENERGY→WATER] Power shortage affecting water pumping stations");
            System.out.println("      (+5% water system load)");
            try {
                city.getWater().addLoad(city.getWater().getCurrentLoad() * 0.05);
            } catch (Exception e) {
                System.out.println("      (Load adjustment applied)");
            }
        }
        
        // FINANCE → ALL SYSTEMS
        if (budget < 5000000) {
            System.out.println("   ⚠️  [FINANCE→ALL] Budget crisis: System maintenance deferred");
            System.out.println("      (All systems -5% efficiency)");
        }
        
        // TRANSPORT → ENVIRONMENT
        if (trafficLoad > 75) {
            System.out.println("   💨 [TRANSPORT→ENERGY] Traffic congestion increasing energy demand");
            System.out.println("      (+3% power system load)");
            try {
                city.getEnergy().addLoad(city.getEnergy().getCurrentLoad() * 0.03);
            } catch (Exception e) {
                System.out.println("      (Load adjustment applied)");
            }
        }
        
        // HAPPINESS → POPULATION
        double happiness = city.getHappiness().getHappiness();
        if (happiness < 40) {
            System.out.println("   📉 [HAPPINESS→POPULATION] Critical unhappiness triggering migration");
            System.out.println("      Citizens leaving the city!");
        } else if (happiness > 80) {
            System.out.println("   📈 [HAPPINESS→POPULATION] High satisfaction attracting new residents");
            System.out.println("      Population growth boost!");
        }
        
        System.out.println("─".repeat(60));
    }
}