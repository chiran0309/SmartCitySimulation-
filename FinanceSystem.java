package systems;

import exceptions.*;
import java.util.*;

public class FinanceSystem extends CitySystem implements SimulationSystem {
    private long cityBudget;
    private long dailyTaxIncome;
    private long operationalExpenses;
    private long netDailyChange;
    private Random random;
    private List<String> transactionHistory;
    
    public FinanceSystem(long initialBudget) 
            throws InvalidParameterException, InvalidCityException {
        super("Finance System", 100000000);
        
        if (initialBudget <= 0) {
            throw new InvalidCityException("Initial budget must be positive");
        }
        
        this.cityBudget = initialBudget;
        this.dailyTaxIncome = (long) (initialBudget * 0.066);
        this.operationalExpenses = (long) (initialBudget * 0.072);
        this.random = new Random();
        this.transactionHistory = new ArrayList<>();
        this.netDailyChange = dailyTaxIncome - operationalExpenses;
    }
    
    @Override
    public void update() throws SystemOverloadException, InvalidParameterException, InsufficientFundsException {
        long incomeVariation = (long) (dailyTaxIncome * (0.9 + random.nextDouble() * 0.2));
        long expenseVariation = (long) (operationalExpenses * (0.95 + random.nextDouble() * 0.1));

        this.dailyTaxIncome = incomeVariation;
        this.operationalExpenses = expenseVariation;
        this.netDailyChange = dailyTaxIncome - operationalExpenses;

        updateBudget();

        double loadPercent = (Math.abs(netDailyChange) / cityBudget) * 100;
        setCurrentLoad(Math.abs(netDailyChange));

        if (netDailyChange < 0 && cityBudget < operationalExpenses) {
            throw new InsufficientFundsException(operationalExpenses, cityBudget);
        }
    }
    
    private void updateBudget() {
        long newBudget = cityBudget + netDailyChange;
        
        if (newBudget < 0) {
            this.status = "CRITICAL";
        } else if (newBudget < cityBudget * 0.3) {
            this.status = "WARNING";
        } else {
            this.status = "STABLE";
        }
        
        this.cityBudget = newBudget;
        recordTransaction();
    }
    
    private void recordTransaction() {
        String transaction = String.format(
                "Income: ৳%,d | Expenses: ৳%,d | Net: ৳%,d | Balance: ৳%,d",
                dailyTaxIncome, operationalExpenses, netDailyChange, cityBudget);
        transactionHistory.add(transaction);
    }
    
    public void deductExpense(long amount) throws InvalidParameterException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidParameterException("amount", "Must be positive");
        }
        if (cityBudget < amount) {
            throw new InsufficientFundsException(amount, cityBudget);
        }
        this.cityBudget -= amount;
        operationalExpenses += amount;
    }
    
    public void deductExpense(long amount, String description) 
            throws InvalidParameterException, InsufficientFundsException {
        deductExpense(amount);
        System.out.println("💰 Expense: " + description + " - ৳" + String.format("%,d", amount));
    }
    
    public void addIncome(long amount) throws InvalidParameterException {
        if (amount <= 0) {
            throw new InvalidParameterException("amount", "Must be positive");
        }
        this.cityBudget += amount;
        this.dailyTaxIncome += amount;
    }
    
    @Override
    public void simulateOneDay() throws SimulationException {
        try {
            System.out.println("\n💳 Finance System");
            System.out.println("-".repeat(40));
            update();
            System.out.printf("Tax revenue collected: ৳ %,d\n", dailyTaxIncome);
            System.out.printf("Operational expenses: ৳ %,d\n", operationalExpenses);
            System.out.printf("Net change today: ৳ %,d\n", netDailyChange);
        } catch (InsufficientFundsException e) {
            System.err.println("💔 " + e.getMessage());
            throw e;
        } catch (SystemOverloadException e) {
            System.err.println("⚠️  " + e.getMessage());
            throw e;
        } catch (InvalidParameterException e) {
            System.err.println("❌ " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    public void handleOverload() {
        System.out.println("🚨 Emergency financial measures activated.");
        System.out.println("📉 Reducing non-essential expenses.");
    }
    
    @Override
    public String getDetailedStatus() {
        return String.format(
                "City Budget             : ৳ %,d\n" +
                "Daily Tax Income        : ৳ %,d\n" +
                "Operational Expenses    : ৳ %,d\n" +
                "Net Daily Change        : ৳ %,d\n" +
                "Status                  : %s",
                cityBudget, dailyTaxIncome, operationalExpenses, netDailyChange, status);
    }
    
    @Override
    public String getReport() {
        return String.format(
                "CITY FINANCE\n" +
                "-".repeat(40) + "\n" +
                "Opening Budget          : ৳ %,d\n" +
                "Daily Tax Collection    : ৳ %,d\n" +
                "Operational Expenses    : ৳ %,d\n" +
                "Net Daily Change        : ৳ %,d\n",
                cityBudget - netDailyChange, dailyTaxIncome, operationalExpenses, netDailyChange);
    }
    
    @Override
    public boolean isCritical() {
        return status.equals("CRITICAL") || cityBudget < operationalExpenses * 2;
    }
    
    @Override
    public void reset() {
        this.dailyTaxIncome = 0;
        this.operationalExpenses = 0;
        this.netDailyChange = 0;
        this.currentLoad = 0;
        this.status = "STABLE";
    }
    
    public long getCityBudget() {
        return cityBudget;
    }
    
    public long getDailyTaxIncome() {
        return dailyTaxIncome;
    }
    
    public long getOperationalExpenses() {
        return operationalExpenses;
    }
    
    public long getNetDailyChange() {
        return netDailyChange;
    }
    
    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
}