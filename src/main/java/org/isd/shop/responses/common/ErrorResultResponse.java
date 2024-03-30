package org.isd.shop.responses.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResultResponse {
    @JsonProperty("error")
    private String error;
}
