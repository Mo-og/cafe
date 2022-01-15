package ua.cafe.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonMaker {
    private static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public static <T> String getJson(T t){
        try {
            return ow.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> ResponseEntity<String> getJsonResponse(T t){
        String json = getJson(t);
        if (json == null)
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
