package sori.jakku.kkunkkyu.memore.exception;

public class DuplicateMemoException extends Exception {

    public DuplicateMemoException() {
    }

    public DuplicateMemoException(String message) {
        super(message);
    }

    public DuplicateMemoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMemoException(Throwable cause) {
        super(cause);
    }
}
