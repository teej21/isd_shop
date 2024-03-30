package org.isd.shop.components;

import lombok.RequiredArgsConstructor;
import org.isd.shop.enums.Enums;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Utils {
    public  boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    public  boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    public  boolean isValidRole(String role) {
        try {
            Enums.Role.valueOf(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public  boolean isValidGender(String gender) {
        try {
            Enums.Gender.valueOf(gender);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
