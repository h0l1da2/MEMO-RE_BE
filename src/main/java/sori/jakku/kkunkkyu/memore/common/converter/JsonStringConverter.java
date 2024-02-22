package sori.jakku.kkunkkyu.memore.common.converter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonStringConverter {
    public static String jsonToString(String json, String value) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(value).getAsString();

    }
}
