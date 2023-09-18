package sori.jakku.kkunkkyu.memore.service.inter;

import jakarta.servlet.http.HttpServletRequest;

public interface WebService {
    String jsonToString(String json, String value);
    String objectToJson(Object obj);
    Long getIdInHeader(HttpServletRequest request);
}
