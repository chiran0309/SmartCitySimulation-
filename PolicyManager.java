package managers;

import entities.City;
import exceptions.*;
import java.util.Scanner;

public class PolicyManager {
    private City city;
    private Scanner scanner;
    
    public PolicyManager(City city, Scanner scanner) {
        this.city = city;
        this.scanner = scanner;
    }
    
    /**
     * Main policy management menu
     */
    public void managePolicies() {
        boolean inPolicy = true;
        
        while (inPolicy) {
            displayPolicyMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    updateTransportPolicy();
                    break;
                case 2:
                    updateEnergyPolicy();
                    break;
                case 3:
                    updateFinancePolicy();
                    break;
                case 4:
                    updateWaterPolicy();
                    break;
                case 5:
                    viewAllPolicies();
                    break;
                case 6:
                    inPolicy = false;
                    System.out.println("✅ Returning to main menu...");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
    
    private void displayPolicyMenu() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(15) + "POLICY MANAGEMENT" + " ".repeat(26) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        System.out.println("\n1️⃣  Update Transport Policy (Add Buses)");
        System.out.println("2️⃣  Update Energy Policy (Production)");
        System.out.println("3️⃣  Update Finance Policy (Taxes)");
        System.out.println("4️⃣  Update Water Policy (Supply)");
        System.out.println("5️⃣  View All Current Policies");
        System.out.println("6️⃣  Return to Main Menu");
        System.out.println("─".repeat(60));
        System.out.print("Choose option (1-6): ");
    }
    
    /**
     * Update transport: Add buses
     */
    private void updateTransportPolicy() {
        System.out.println("\n🚗 TRANSPORT POLICY UPDATE");
        System.out.println("─".repeat(60));
        System.out.printf("Current Buses: %d / 500\n", city.getTransport().getBusesActive());
        System.out.print("Enter number of buses to add (0-100): ");
        
        try {
            int busesToAdd = getIntInput();
            
            if (busesToAdd < 0 || busesToAdd > 100) {
                System.out.println("❌ Invalid amount. Must be 0-100.");
                return;
            }
            
            city.getTransport().addBuses(busesToAdd, "Policy Update");
            city.recordDay("Transport Policy: Added " + busesToAdd + " buses");
            
            System.out.println("✅ Transport policy updated!");
            System.out.printf("New total buses: %d\n", city.getTransport().getBusesActive());
            
        } catch (InvalidParameterException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }
    
    /**
     * Update energy: Increase production
     */
    private void updateEnergyPolicy() {
        System.out.println("\n⚡ ENERGY POLICY UPDATE");
        System.out.println("─".repeat(60));
        System.out.printf("Current Production: %.1f / 1000.0\n", city.getEnergy().getPowerProduction());
        System.out.print("Enter additional production to add (0-200): ");
        
        try {
            double productionToAdd = Double.parseDouble(scanner.nextLine());
            
            if (productionToAdd < 0 || productionToAdd > 200) {
                System.out.println("❌ Invalid amount. Must be 0-200.");
                return;
            }
            
            city.recordDay("Energy Policy: Added " + productionToAdd + " production units");
            
            System.out.println("✅ Energy policy updated!");
            System.out.println("⚠️  Note: Production increase takes effect next simulation day.");
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid input. Enter a number.");
        }
    }
    
    /**
     * Update finance: Adjust tax rate
     */
    private void updateFinancePolicy() {
        System.out.println("\n💰 FINANCE POLICY UPDATE");
        System.out.println("─".repeat(60));
        System.out.printf("Current Budget: ৳%,d\n", city.getFinance().getCityBudget());
        System.out.println("\nTax Policy Options:");
        System.out.println("1. Increase taxes by 5% (+more income, -happiness)");
        System.out.println("2. Decrease taxes by 5% (-less income, +happiness)");
        System.out.println("3. Emergency fund injection (use reserve)");
        System.out.print("Choose option (1-3): ");
        
        try {
            int option = getIntInput();
            
            switch (option) {
                case 1:
                    city.recordDay("Finance Policy: Tax increase +5%");
                    System.out.println("✅ Tax increased by 5%");
                    System.out.println("📉 Citizen happiness may decrease.");
                    break;
                case 2:
                    city.recordDay("Finance Policy: Tax decrease -5%");
                    System.out.println("✅ Tax decreased by 5%");
                    System.out.println("📈 Citizen happiness may increase.");
                    break;
                case 3:
                    System.out.print("Enter amount to inject (০-1000000): ");
                    long amount = Long.parseLong(scanner.nextLine());
                    if (amount > 0 && amount <= 1000000) {
                        city.getFinance().addIncome(amount);
                        city.recordDay("Finance Policy: Emergency fund injection ৳" + amount);
                        System.out.println("✅ Emergency fund injected: ৳" + amount);
                    } else {
                        System.out.println("❌ Invalid amount.");
                    }
                    break;
                default:
                    System.out.println("❌ Invalid option.");
            }
        } catch (Exception e) {
            System.err.println("❌ " + e.getMessage());
        }
    }
    
    /**
     * Update water: Increase supply
     */
    private void updateWaterPolicy() {
        System.out.println("\n💧 WATER POLICY UPDATE");
        System.out.println("─".repeat(60));
        System.out.printf("Current Supply: %.1f / 500.0\n", city.getWater().getWaterSupplyCapacity());
        System.out.print("Enter additional water supply (0-100): ");
        
        try {
            double supplyToAdd = Double.parseDouble(scanner.nextLine());
            
            if (supplyToAdd < 0 || supplyToAdd > 100) {
                System.out.println("❌ Invalid amount. Must be 0-100.");
                return;
            }
            
            city.recordDay("Water Policy: Added " + supplyToAdd + " supply units");
            
            System.out.println("✅ Water policy updated!");
            System.out.println("⚠️  Note: Supply increase takes effect next simulation day.");
            
        } catch (NumberFormatException e) {
            System.err.println("❌ Invalid input. Enter a number.");
        }
    }
    
    /**
     * View all current policies and settings
     */
    private void viewAllPolicies() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📋 CURRENT CITY POLICIES & SETTINGS");
        System.out.println("═".repeat(60));
        
        System.out.println("\n🚗 TRANSPORT POLICY:");
        System.out.printf("  • Active Buses: %d / 500\n", city.getTransport().getBusesActive());
        System.out.printf("  • Traffic Level: %s\n", city.getTransport().getTrafficLevel());
        System.out.printf("  • Avg Travel Time: %.0f min\n", city.getTransport().getAverageTravelTime());
        
        System.out.println("\n⚡ ENERGY POLICY:");
        System.out.printf("  • Production: %.1f / 1000\n", city.getEnergy().getPowerProduction());
        System.out.printf("  • Usage: %.1f%%\n", (city.getEnergy().getPowerUsage() / city.getEnergy().getPowerProduction()) * 100);
        System.out.printf("  • Grid Status: %s\n", city.getEnergy().getGridStatus());
        
        System.out.println("\n💰 FINANCE POLICY:");
        System.out.printf("  • City Budget: ৳%,d\n", city.getFinance().getCityBudget());
        System.out.printf("  • Daily Tax Income: ৳%,d\n", city.getFinance().getDailyTaxIncome());
        System.out.printf("  • Daily Expenses: ৳%,d\n", city.getFinance().getOperationalExpenses());
        
        System.out.println("\n💧 WATER POLICY:");
        System.out.printf("  • Supply Capacity: %.1f / 500\n", city.getWater().getWaterSupplyCapacity());
        System.out.printf("  • Consumption: %.1f%%\n", (city.getWater().getWaterConsumption() / city.getWater().getWaterSupplyCapacity()) * 100);
        
        System.out.println("\n😊 HAPPINESS POLICY:");
        System.out.printf("  • Citizen Happiness: %.0f / 100\n", city.getHappiness().getHappiness());
        System.out.printf("  • Mood Status: %s\n", city.getHappiness().getMoodStatus());
        
        System.out.println("═".repeat(60));
    }
    
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("⚠️  Invalid input. Enter a number.");
            return -1;
        }
    }
}