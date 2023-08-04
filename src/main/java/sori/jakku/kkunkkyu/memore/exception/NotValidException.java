package sori.jakku.kkunkkyu.memore.exception;

public class NotValidException extends Exception {
    public NotValidException() {
    }

    public NotValidException(String message) {
        super(message);
    }

    public NotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidException(Throwable cause) {
        super(cause);
    }
}
