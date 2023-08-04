package sori.jakku.kkunkkyu.memore.exception;

public class UsernameNotValidException extends Exception {
    public UsernameNotValidException() {
    }

    public UsernameNotValidException(String message) {
        super(message);
    }

    public UsernameNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameNotValidException(Throwable cause) {
        super(cause);
    }
}
