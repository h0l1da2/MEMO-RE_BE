package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import sori.jakku.kkunkkyu.memore.domain.dto.Response;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final WebService webService;

    // @Valid 관련 Exception
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> invalidRequestHandler(MethodArgumentNotValidException e) {

        JsonObject jsonObject = new JsonObject();

        for (FieldError error : e.getFieldErrors()) {
            if (error.getField().equals("username")) {
                jsonObject.addProperty("response", Response.BAD_USERNAME);
            }
            if (error.getField().equals("password")) {
                jsonObject.addProperty("response", Response.BAD_PWD);
            }
            if (error.getField().equals("keyword")) {
                jsonObject.addProperty("response", Response.BAD);
            }
            if (error.getField().equals("tag") && e.getMessage().contains("size")) {
                jsonObject.addProperty("response", Response.SIZE);
            }
            if (error.getField().equals("tag")) {
                jsonObject.addProperty("response", Response.TAG_NOT_VALID);
            }
        }

        return webService.badResponse(jsonObject);
    }

}
