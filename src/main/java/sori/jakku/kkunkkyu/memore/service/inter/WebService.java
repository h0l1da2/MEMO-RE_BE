package sori.jakku.kkunkkyu.memore.service.inter;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;

public interface WebService {
    ResponseEntity<String> okResponse(JsonObject jsonObject);
    ResponseEntity<String> badResponse(JsonObject jsonObject);
    String jsonToString(String json, String value);
}
