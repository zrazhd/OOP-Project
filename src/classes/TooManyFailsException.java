package classes;

public class TooManyFailsException extends Exception {
    public TooManyFailsException(String message) {
        super(message);
    }
}
