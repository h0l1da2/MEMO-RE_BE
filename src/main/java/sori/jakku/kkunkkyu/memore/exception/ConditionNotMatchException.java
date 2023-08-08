package sori.jakku.kkunkkyu.memore.exception;

public class ConditionNotMatchException extends Exception {

    public ConditionNotMatchException() {
    }

    public ConditionNotMatchException(String message) {
        super(message);
    }

    public ConditionNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionNotMatchException(Throwable cause) {
        super(cause);
    }
}
