package ua.cafe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ResponseFactory {

    public static ResponseEntity<HashMap<String, List<String>>> createResponse(BindingResult result) {
        if (result.hasErrors()) {
            var ErrorsMap = new HashMap<String, List<String>>();
            for (FieldError e : result.getFieldErrors())
                ErrorsMap.put(e.getField(), Collections.singletonList(e.getDefaultMessage()));
            return new ResponseEntity<>(ErrorsMap, HttpStatus.NOT_ACCEPTABLE);
        }
        return null;
    }
}
