package sori.jakku.kkunkkyu.memore.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sori.jakku.kkunkkyu.memore.service.inter.WebService;

@Service
@RequiredArgsConstructor
public class WebServiceImpl implements WebService {

    @Override
    public ResponseEntity<String> okResponse(JsonObject jsonObject) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(json);
    }

    @Override
    public ResponseEntity<String> badResponse(JsonObject jsonObject) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        return ResponseEntity.badRequest()
                .body(json);
    }
}
