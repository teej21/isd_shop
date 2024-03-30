package org.isd.shop.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.isd.shop.responses.common.ResultResponse;

public class UserSignupResponse extends ResultResponse {
    public UserSignupResponse(String result) {
        super(result);
    }
}
