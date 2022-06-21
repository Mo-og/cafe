package ua.cafe.models;

import org.springframework.http.ResponseEntity;
import ua.cafe.utils.JsonMaker;

import java.util.HashMap;

public enum ReadyStatus {
    NEW("Новий"), IN_PROGRESS("Готується"), READY("Приготовано"), FINISHED("Выконано"), PAID("Сплачено");
    public final String description;

    public static final ResponseEntity<String> MAP_RESPONSE_ENTITY;

    static {
        var options = ReadyStatus.values();
        HashMap<ReadyStatus, String> map = new HashMap<>();
        for (var val : options)
            map.put(val, val.description);
        MAP_RESPONSE_ENTITY = JsonMaker.getJsonResponse(map);
    }

    ReadyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}
