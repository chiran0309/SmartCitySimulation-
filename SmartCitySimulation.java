import managers.*;
import entities.*;
import exceptions.*;
import java.util.*;

public class SmartCitySimulation {
    private SimulationEngine engine;
    private Scanner scanner;
    private boolean running;
    private PolicyManager policyManager;
    
    public SmartCitySimulation() {
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        displayWelcome();
        initializeCity();
        mainMenu();
        cleanup();
    }
    
    private void displayWelcome() {
        System.out.println("\n╔" + "═".repeat(48) + "╗");
        System.out.println("║  🏙 SMART CITY SIMULATION v2.0                     ║");
        System.out.println("╚" + "═".repeat(48) + "╝");
    }
    
    private void initializeCity() {
        try {
            System.out.println("\n📋 Initializing Smart City...\n");
            
            // Try to load saved data
            Map<String, String> savedData = CityDataManager.loadCity();
            
            int population = savedData.isEmpty() ? 25000 : Integer.parseInt(savedData.getOrDefault("Population", "25000"));
            int buses = savedData.isEmpty() ? 120 : Integer.parseInt(savedData.getOrDefault("Active Buses", "120"));
            double energy = savedData.isEmpty() ? 820.0 : Double.parseDouble(savedData.getOrDefault("Energy Production", "820.0"));
            double water = savedData.isEmpty() ? 400.0 : Double.parseDouble(savedData.getOrDefault("Water Supply", "400.0"));
            long budget = savedData.isEmpty() ? 13200000L : Long.parseLong(savedData.getOrDefault("Budget", "13200000"));
            
            City city = new City("NovaTown", population, buses, energy, water, budget);
            this.engine = new SimulationEngine(city);
            this.policyManager = new PolicyManager(city, scanner);
            
            System.out.println("✅ City 'NovaTown' initialized successfully!");
            
        } catch (InvalidParameterException | InvalidCityException e) {
            System.err.println("❌ Failed to initialize city: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void mainMenu() {
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput();
                handleMenuChoice(choice);
            } catch (InputMismatchException e) {
                System.err.println("⚠️  Invalid input. Please enter a number.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n" + "╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(13) + "SMART CITY MANAGEMENT" + " ".repeat(24) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        System.out.printf("\n🏙 %s | Day %d | Population: %,d\n", 
            engine.getCity().getCityName(),
            engine.getCity().getCurrentDay(),
            engine.getCity().getPopulation().getTotalPopulation());
        System.out.println("\n" + "─".repeat(60));
        System.out.println("1️⃣  View Enhanced Dashboard");
        System.out.println("2️⃣  Run One-Day Simulation");
        System.out.println("3️⃣  View System Alerts");
        System.out.println("4️⃣  View Detailed Report");
        System.out.println("5️⃣  View Trends & Charts");
        System.out.println("6️⃣  View Event Log");
        System.out.println("7️⃣  Change Scenario");
        System.out.println("8️⃣  📋 MANAGE POLICIES (UPDATE SETTINGS)");
        System.out.println("9️⃣  💾 SAVE CITY DATA");
        System.out.println("🔟 📂 VIEW SAVED DATA");
        System.out.println("1️⃣1️⃣  🔄 RESTORE FROM BACKUP");
        System.out.println("1️⃣2️⃣  Exit");
        System.out.println("─".repeat(60));
        System.out.print("Choose option (1-12): ");
    }
    
    private void handleMenuChoice(int choice) throws SimulationException {
        System.out.println();
        
        switch (choice) {
            case 1:
                handleViewEnhancedDashboard();
                break;
            case 2:
                handleRunSimulation();
                break;
            case 3:
                handleViewAlerts();
                break;
            case 4:
                handleViewReport();
                break;
            case 5:
                handleViewTrends();
                break;
            case 6:
                handleViewEventLog();
                break;
            case 7:
                handleChangeScenario();
                break;
            case 8:
                handleManagePolicies();
                break;
            case 9:
                handleSaveCity();
                break;
            case 10:
                handleViewSavedData();
                break;
            case 11:
                handleRestoreBackup();
                break;
            case 12:
                handleExit();
                break;
            default:
                System.out.println("❌ Invalid choice. Please select 1-12.");
        }
    }
    
    // NEW METHOD: Manage Policies
    private void handleManagePolicies() {
        System.out.println("8️⃣ Policy Management");
        policyManager.managePolicies();
    }
    
    // NEW METHOD: Save City
    private void handleSaveCity() {
        System.out.println("9️⃣ Saving City Data");
        CityDataManager.saveCity(engine.getCity());
    }
    
    // NEW METHOD: View Saved Data
    private void handleViewSavedData() {
        System.out.println("🔟 View Saved Data");
        CityDataManager.displaySavedData();
    }
    
    // NEW METHOD: Restore Backup
    private void handleRestoreBackup() {
        System.out.println("1️⃣1️⃣ Restore from Backup");
        CityDataManager.restoreFromBackup();
    }
    
    // EXISTING METHODS
    private void handleViewEnhancedDashboard() {
        System.out.println("1️⃣ Enhanced Dashboard");
        engine.getCity().displayCityStatusEnhanced();
    }
    
    private void handleRunSimulation() throws SimulationException {
        System.out.println("2️⃣ Run One-Day Simulation");
        try {
            engine.runOneDay();
        } catch (SimulationException e) {
            System.err.println("\n⚠️  Simulation encountered an issue: " + e.getMessage());
            System.out.println("Would you like to continue? (y/n): ");
            if (!scanner.nextLine().toLowerCase().startsWith("y")) {
                running = false;
            }
        }
    }
    
    private void handleViewAlerts() {
        System.out.println("3️⃣ View System Alerts");
        engine.displayAlerts();
    }
    
    private void handleViewReport() {
        System.out.println("4️⃣ View Detailed Simulation Report");
        engine.displayReport();
    }
    
    private void handleViewTrends() {
        System.out.println("5️⃣ Trends & Charts");
        engine.getCity().displayTrends();
    }
    
    private void handleViewEventLog() {
        System.out.println("6️⃣ Event Log");
        engine.getCity().displayHistory();
    }
    
    private void handleChangeScenario() {
        System.out.println("7️⃣ Scenario System\n");
        System.out.println("Select Simulation Scenario:\n");
        System.out.println("1 Normal Day");
        System.out.println("2 Festival Day");
        System.out.println("3 Power Crisis");
        System.out.println("4 Transport Strike");
        System.out.print("\nEnter scenario (1-4): ");
        
        try {
            int choice = getIntInput();
            String scenario = switch (choice) {
                case 1 -> "Normal Day";
                case 2 -> "Festival Day";
                case 3 -> "Power Crisis";
                case 4 -> "Transport Strike";
                default -> throw new InvalidParameterException("scenario", 
                        "Invalid choice: " + choice);
            };
            
            engine.setScenario(scenario);
        } catch (InvalidParameterException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }
    private void handleExit() {
        System.out.println("1️⃣2️⃣ Exit");
        System.out.println("Saving simulation data...\n");
        CityDataManager.saveCity(engine.getCity());
        running = false;
    }
    
    private int getIntInput() throws InputMismatchException {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new InputMismatchException();
        }
    }
    
    private void cleanup() {
        scanner.close();
        System.out.println("\nThank you for using Smart City Simulation.");
        System.out.println("Goodbye! 👋\n");
    }
    
    public static void main(String[] args) {
        SmartCitySimulation simulation = new SmartCitySimulation();
        simulation.start();
    }
}
