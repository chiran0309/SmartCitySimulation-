package systems;

import entities.City;
import exceptions.*;
import java.util.Random;

public abstract class RandomEvent {
    protected String description;
    protected String emoji;
    
    public abstract String getDescription();
    public abstract String getEmoji();
    public abstract void apply(City city) throws SimulationException;
    
    // Factory method - creates random events
    public static RandomEvent generateEvent() {
        Random rand = new Random();
        int eventChance = rand.nextInt(100);
        
        if (eventChance < 5) return new FestivalEvent();
        if (eventChance < 10) return new BlackoutEvent();
        if (eventChance < 15) return new FloodEvent();
        if (eventChance < 20) return new PopulationSurgeEvent();
        if (eventChance < 22) return new TransportCrashEvent();
        
        return null; // No event
    }
}

class FestivalEvent extends RandomEvent {
    @Override
    public String getDescription() {
        return "🎉 FESTIVAL EVENT: Increased tourism and happiness! +25% Transport demand, +12% Energy use, +$500K income";
    }
    
    @Override
    public String getEmoji() {
        return "🎉";
    }
    
    @Override
    public void apply(City city) throws SimulationException {
        try {
            city.recordDay("Festival Event - Tourism increased, transport demand +25%");
            city.getTransport().addBuses(15, "Festival Support");
            city.getFinance().addIncome(500000);
            System.out.println("✨ Festival boost: +500,000 ৳ in tourism revenue!");
        } catch (InvalidParameterException e) {
            System.err.println("Festival event error: " + e.getMessage());
        }
    }
}

class BlackoutEvent extends RandomEvent {
    @Override
    public String getDescription() {
        return "⚡ BLACKOUT EVENT: City-wide power failure! -30% energy production, +$200K emergency costs";
    }
    
    @Override
    public String getEmoji() {
        return "⚡";
    }
    
    @Override
    public void apply(City city) throws SimulationException {
        try {
            city.recordDay("Blackout Event - Power grid failure");
            city.getEnergy().handleOverload();
            city.getFinance().deductExpense(200000, "Blackout Recovery");
            System.out.println("🔴 EMERGENCY: Power outage costs 200,000 ৳!");
        } catch (InvalidParameterException | InsufficientFundsException e) {
            System.err.println("Blackout recovery error: " + e.getMessage());
        }
    }
}

class FloodEvent extends RandomEvent {
    @Override
    public String getDescription() {
        return "🌊 FLOOD EVENT: Heavy flooding damages infrastructure! Water system stressed, transport chaos, -$300K";
    }
    
    @Override
    public String getEmoji() {
        return "🌊";
    }
    
    @Override
    public void apply(City city) throws SimulationException {
        try {
            city.recordDay("Flood Event - Infrastructure damage");
            city.getWater().handleOverload();
            city.getTransport().handleOverload();
            city.getFinance().deductExpense(300000, "Flood Damage Recovery");
            System.out.println("🚨 DISASTER: Flooding damage costs 300,000 ৳!");
        } catch (InvalidParameterException | InsufficientFundsException e) {
            System.err.println("Flood recovery error: " + e.getMessage());
        }
    }
}

class PopulationSurgeEvent extends RandomEvent {
    @Override
    public String getDescription() {
        return "📈 POPULATION SURGE: New residents moving in! +2% population growth, +$400K tax revenue";
    }
    
    @Override
    public String getEmoji() {
        return "📈";
    }
    
    @Override
    public void apply(City city) throws SimulationException {
        try {
            city.recordDay("Population Surge - +2% growth");
            city.getFinance().addIncome(400000);
            System.out.println("🏘️ Growth: +400,000 ৳ in new tax revenue!");
        } catch (InvalidParameterException e) {
            System.err.println("Population surge error: " + e.getMessage());
        }
    }
}

class TransportCrashEvent extends RandomEvent {
    @Override
    public String getDescription() {
        return "🚗 TRANSPORT INCIDENT: Major accident on highway! -50 buses, +$150K costs, traffic chaos";
    }
    
    @Override
    public String getEmoji() {
        return "🚗";
    }
    
    @Override
    public void apply(City city) throws SimulationException {
        try {
            city.recordDay("Transport Crash - Bus fleet damaged");
            city.getTransport().handleOverload();
            city.getFinance().deductExpense(150000, "Transport Incident Recovery");
            System.out.println("⚠️ ACCIDENT: Damages cost 150,000 ৳!");
        } catch (InvalidParameterException | InsufficientFundsException e) {
            System.err.println("Accident recovery error: " + e.getMessage());
        }
    }
}