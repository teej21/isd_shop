package org.isd.shop.components;

import lombok.RequiredArgsConstructor;
import org.isd.shop.enums.Enums;
import org.isd.shop.responses.common.ErrorResultResponse;
import org.isd.shop.responses.common.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Utils {

    private final JwtTokenUtil jwtTokenUtil;

    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    public boolean isValidRole(String role) {
        try {
            Enums.Role.valueOf(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidGender(String gender) {
        try {
            Enums.Gender.valueOf(gender);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String handleBindingResultError(BindingResult bindingResult) {
        List<String> errorMessages = bindingResult.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();
        return errorMessages.get(0);
    }

    public ResponseEntity<ErrorResultResponse> ErrorResponse(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResultResponse(e.getMessage()));
    }

    public ResponseEntity<ResultResponse> ResultResponse(String result) {
        return ResponseEntity.ok().body(new ResultResponse(result));
    }



}
