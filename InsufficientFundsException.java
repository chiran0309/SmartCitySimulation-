package exceptions;

public class InsufficientFundsException extends SimulationException {
    public InsufficientFundsException(long required, long available) {
        super(String.format("Insufficient funds. Required: ৳%,d, Available: ৳%,d", 
              required, available));
    }
}