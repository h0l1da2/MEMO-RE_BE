package sori.jakku.kkunkkyu.memore.common.exception;

public class UsernameDuplException extends Exception {
    public UsernameDuplException() {
    }

    public UsernameDuplException(String message) {
        super(message);
    }

    public UsernameDuplException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameDuplException(Throwable cause) {
        super(cause);
    }
}
