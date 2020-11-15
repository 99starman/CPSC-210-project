package exception;

public class NotPositiveException extends AccountBalanceException {
    public NotPositiveException(String message) {
        super(message);
    }
}
