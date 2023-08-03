package com.example.shorty.utils;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;

public class ResponseHandler {

    public static Object getDataFieldFromResponse(ResponseEntity<Object> responseEntity, String dataFieldName) {
        final Object responseBody =  responseEntity.getBody();
        if (responseBody == null) {
            return null;
        }

        @SuppressWarnings("rawtypes")
        final HashMap responseData = (HashMap) responseBody;

        return responseData.get(dataFieldName);
    }
}
