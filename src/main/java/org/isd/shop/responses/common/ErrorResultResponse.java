package org.isd.shop.responses.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class ErrorResultResponse {
    @JsonProperty("error")
    private String error;

    @Override
    public String toString() {
        return "{" +
                "\"error\":\"" + error + '\"' +
                '}';
    }
}
