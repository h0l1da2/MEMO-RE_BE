package sori.jakku.kkunkkyu.memore.common.handler;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // @Valid 관련 Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> invalidRequestHandler(MethodArgumentNotValidException e) {

        Map<String, String> resultMap = new HashMap<>();

        for (FieldError error : e.getFieldErrors()) {
            resultMap.put(error.getField(), e.getMessage());
        }

        return resultMap;
    }

}
