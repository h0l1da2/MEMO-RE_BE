package sori.jakku.kkunkkyu.memore.handler;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sori.jakku.kkunkkyu.memore.web.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 관련 Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> invalidRequestHandler(MethodArgumentNotValidException e) {

        JsonObject jsonObject = new JsonObject();

        for (FieldError error : e.getFieldErrors()) {
            if (error.getField().equals("username")) {
                jsonObject.addProperty("response", "BAD_USERNAME");
            }
            if (error.getField().equals("password")) {
                jsonObject.addProperty("response", "BAD_PWD");
            }
            if (error.getField().equals("keyword")) {
                jsonObject.addProperty("response", "BAD");
            }
            if (error.getField().equals("tag") && e.getMessage().contains("size")) {
                jsonObject.addProperty("response", "SIZE");
            }
            if (error.getField().equals("tag")) {
                jsonObject.addProperty("response", "TAG_NOT_VALID");
            }
        }

        return Response.badRequest(jsonObject.toString());
    }

}
