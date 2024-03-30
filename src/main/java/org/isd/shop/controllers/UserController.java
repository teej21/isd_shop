package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.dtos.UserDTO;
import org.isd.shop.responses.common.ErrorResultResponse;
import org.isd.shop.responses.user.UserSignupResponse;
import org.isd.shop.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerNewCustomer(
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception("Invalid data");
            }
            String fullName = userDTO.getFullName();
            String email = userDTO.getEmail();
            String phoneNumber = userDTO.getPhoneNumber();
            String password = userDTO.getPassword();
            String gender = userDTO.getGender();
            String role = userDTO.getRole();

            if (isValidString(List.of(fullName, email, phoneNumber, password, gender, role))) {
                throw new Exception("Invalid data");
            }

            return ResponseEntity.ok(userService.registerNewCustomer(
                    fullName,
                    email,
                    phoneNumber,
                    password,
                    gender,
                    role
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResultResponse(e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception("Invalid data");
            }

            String username = userDTO.getUsername();
            String password = userDTO.getPassword();
            String role = userDTO.getRole();
            if (isValidString(List.of(username, password, role))) {
                throw new Exception("Invalid data");
            }
            return ResponseEntity.ok(userService.login(username, password, role));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResultResponse(e.getMessage()));
        }
    }

    /**
     * check if any string is null or empty
     *
     * @param str
     * @return
     */
    private boolean isValidString(List<String> str) {
        return str.stream().anyMatch(s -> s == null || s.isEmpty());
    }
}
