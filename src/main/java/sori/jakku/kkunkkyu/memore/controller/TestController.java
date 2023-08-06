package sori.jakku.kkunkkyu.memore.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sori.jakku.kkunkkyu.memore.config.jwt.TokenService;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TokenService tokenService;
    private final WebService webService;

    @PostMapping("/token")
    public ResponseEntity<String> tokenTest(HttpServletRequest request, HttpServletResponse response) {
        JsonObject jsonObject = new JsonObject();

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            jsonObject.addProperty("response", "FAILED");
            return webService.badResponse(jsonObject);
        }
        header = header.substring("Bearer ".length());
        String token = tokenService.reCreateToken(header);

        jsonObject.addProperty("token", token);
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);

        return webService.okResponse(jsonObject);

    }

}
