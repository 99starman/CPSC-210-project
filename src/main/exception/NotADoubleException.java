package exception;

public class NotADoubleException extends AccountBalanceException {
    public NotADoubleException(String message) {
        super(message);
    }
}
