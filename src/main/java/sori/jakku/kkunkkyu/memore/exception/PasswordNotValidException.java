package sori.jakku.kkunkkyu.memore.exception;

public class PasswordNotValidException extends Exception {
    public PasswordNotValidException() {
    }

    public PasswordNotValidException(String message) {
        super(message);
    }

    public PasswordNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordNotValidException(Throwable cause) {
        super(cause);
    }
}
