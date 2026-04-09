package managers;

import entities.City;
import exceptions.*;
import java.io.*;
import java.util.*;

public class CityDataManager {
    private static final String DATA_FILE = "city_data.txt";
    private static final String BACKUP_FILE = "city_data_backup.txt";
    
    /**
     * Save city state to file
     */
    public static void saveCity(City city) {
        try {
            // Create backup first
            createBackup();
            
            // Write new data
            FileWriter writer = new FileWriter(DATA_FILE);
            writer.write("=== SMART CITY DATA ===\n");
            writer.write("City Name," + city.getCityName() + "\n");
            writer.write("Current Day," + city.getCurrentDay() + "\n");
            writer.write("Population," + city.getPopulation().getTotalPopulation() + "\n");
            writer.write("Budget," + city.getFinance().getCityBudget() + "\n");
            writer.write("Happiness," + city.getHappiness().getHappiness() + "\n");
            writer.write("Active Buses," + city.getTransport().getBusesActive() + "\n");
            writer.write("Energy Production," + city.getEnergy().getPowerProduction() + "\n");
            writer.write("Water Supply," + city.getWater().getWaterSupplyCapacity() + "\n");
            writer.write("Save Time," + new java.util.Date() + "\n");
            writer.close();
            
            System.out.println("✅ City data saved successfully!");
            
        } catch (IOException e) {
            System.err.println("❌ Error saving city data: " + e.getMessage());
        }
    }
    
    /**
     * Load city state from file
     */
    public static Map<String, String> loadCity() {
        Map<String, String> data = new HashMap<>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
            String line;
            
            while ((line = reader.readLine()) != null) {
                if (line.contains(",") && !line.startsWith("===")) {
                    String[] parts = line.split(",", 2);
                    if (parts.length == 2) {
                        data.put(parts[0].trim(), parts[1].trim());
                    }
                }
            }
            reader.close();
            
            if (!data.isEmpty()) {
                System.out.println("✅ City data loaded successfully!");
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("ℹ️  No previous city data found. Starting fresh.");
        } catch (IOException e) {
            System.err.println("❌ Error loading city data: " + e.getMessage());
        }
        
        return data;
    }
    
    /**
     * Create backup before saving
     */
    private static void createBackup() {
        try {
            File original = new File(DATA_FILE);
            if (original.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
                BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_FILE));
                
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                
                reader.close();
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("⚠️  Could not create backup: " + e.getMessage());
        }
    }
    
    /**
     * Display saved city data
     */
    public static void displaySavedData() {
        Map<String, String> data = loadCity();
        
        if (data.isEmpty()) {
            System.out.println("❌ No saved city data found.");
            return;
        }
        
        System.out.println("\n" + "═".repeat(60));
        System.out.println("💾 SAVED CITY DATA");
        System.out.println("═".repeat(60));
        
        data.forEach((key, value) -> {
            System.out.printf("%-20s : %s\n", key, value);
        });
        
        System.out.println("═".repeat(60));
    }
    
    /**
     * Delete saved data
     */
    public static void deleteSavedData() {
        try {
            File file = new File(DATA_FILE);
            if (file.delete()) {
                System.out.println("✅ Saved city data deleted.");
            } else {
                System.out.println("❌ No data to delete.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error deleting data: " + e.getMessage());
        }
    }
    
    /**
     * Restore from backup
     */
    public static void restoreFromBackup() {
        try {
            File backup = new File(BACKUP_FILE);
            File original = new File(DATA_FILE);
            
            if (backup.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(BACKUP_FILE));
                BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE));
                
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }
                
                reader.close();
                writer.close();
                
                System.out.println("✅ City data restored from backup!");
            } else {
                System.out.println("❌ No backup available.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error restoring backup: " + e.getMessage());
        }
    }
}