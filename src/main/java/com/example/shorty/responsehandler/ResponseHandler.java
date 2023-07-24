package com.example.shorty.responsehandler;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ResponseHandler {

    public static Object getDataFieldFromResponse(ResponseEntity<Object> responseEntity, String dataFieldName) {
        Object responseBody =  responseEntity.getBody();
        if (responseBody == null) {
            return null;
        }

        HashMap responseData = (HashMap) responseBody;

        return responseData.get(dataFieldName);
    }
}
