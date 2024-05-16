package org.isd.shop.responses.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class ResultResponse {
    @JsonProperty("result")
    private String result;
}
