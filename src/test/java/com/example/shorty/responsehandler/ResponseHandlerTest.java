package com.example.shorty.responsehandler;

import com.example.shorty.utils.ResponseHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseHandlerTest {

    @Test
    void getDataFieldFromResponse() {
        Map<String, Object> data = new HashMap<>();
        data.put("success", false);
        data.put("number", 1);
        data.put("name", "karlo1");
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(data, HttpStatus.OK);

        Object testObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "success");
        assertThat(testObject).isEqualTo(false);

        testObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "number");
        assertThat(testObject).isEqualTo(1);

        testObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "name");
        assertThat(testObject).isEqualTo("karlo1");

        responseEntity = new ResponseEntity<>(HttpStatus.OK);
        testObject = ResponseHandler.getDataFieldFromResponse(responseEntity, "invalid");
        assertThat(testObject).isEqualTo(null);
    }
}