package ua.cafe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Utils {
    private static final DateFormat dateFormat = new SimpleDateFormat("_-_dd.MM.yyyy_HH.mm.ss");

    public static String getDateString() {
        return dateFormat.format(new Date());
    }

    public static ResponseEntity<HashMap<String, List<String>>> getResponseEntity(BindingResult result) {
        if (result.hasErrors()) {
            var ErrorsMap = new HashMap<String, List<String>>();
            for (FieldError e : result.getFieldErrors())
                ErrorsMap.put(e.getField(), Collections.singletonList(e.getDefaultMessage()));
            return new ResponseEntity<>(ErrorsMap, HttpStatus.NOT_ACCEPTABLE);
        }
        return null;
    }
}
