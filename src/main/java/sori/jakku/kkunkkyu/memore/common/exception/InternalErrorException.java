package sori.jakku.kkunkkyu.memore.common.exception;

public class InternalErrorException extends RuntimeException {
    private final int code;

    public InternalErrorException(Exception exception){
        super(exception.getMessage());
        code = exception.getCode();
    }
}
