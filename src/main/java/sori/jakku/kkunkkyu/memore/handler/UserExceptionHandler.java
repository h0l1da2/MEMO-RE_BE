package sori.jakku.kkunkkyu.memore.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sori.jakku.kkunkkyu.memore.exception.UserNotFoundException;
import sori.jakku.kkunkkyu.memore.exception.UsernameDuplException;
import sori.jakku.kkunkkyu.memore.web.Response;

import javax.security.auth.login.LoginException;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> userNotFoundHandler(UserNotFoundException e) {
        return Response.badRequest(e.getMessage());
    }
    @ExceptionHandler(UsernameDuplException.class)
    public ResponseEntity<Response> usernameDuplicateHandler(UsernameDuplException e) {
        return Response.badRequest(e.getMessage());
    }
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Response> loginExceptionHandler(LoginException e) {
        return Response.badRequest(e.getMessage());
    }

}
