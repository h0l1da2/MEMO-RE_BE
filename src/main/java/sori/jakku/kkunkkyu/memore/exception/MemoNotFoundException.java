package sori.jakku.kkunkkyu.memore.exception;

public class MemoNotFoundException extends Exception {
    public MemoNotFoundException() {
    }

    public MemoNotFoundException(String message) {
        super(message);
    }

    public MemoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemoNotFoundException(Throwable cause) {
        super(cause);
    }
}
