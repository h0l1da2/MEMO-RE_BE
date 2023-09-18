package sori.jakku.kkunkkyu.memore.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sori.jakku.kkunkkyu.memore.exception.DuplicateMemoException;
import sori.jakku.kkunkkyu.memore.exception.MemoNotFoundException;
import sori.jakku.kkunkkyu.memore.web.Response;

@ControllerAdvice
public class MemoExceptionHandler {

    @ExceptionHandler(MemoNotFoundException.class)
    public ResponseEntity<Response> memoNotFoundHandler(MemoNotFoundException e) {
        return Response.badRequest(e.getMessage());
    }
    @ExceptionHandler(DuplicateMemoException.class)
    public ResponseEntity<Response> duplicateMemoHandler(DuplicateMemoException e) {
        return Response.badRequest(e.getMessage());
    }
}
