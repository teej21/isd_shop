package org.isd.shop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.isd.shop.dtos.UseRegisterDTO;
import org.isd.shop.dtos.UserLoginDTO;
import org.isd.shop.dtos.UserLoginResponseDTO;
import org.isd.shop.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UseRegisterDTO userDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            userService.createUser(userDTO);
            return ResponseEntity.ok("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) {
        try {

            UserLoginResponseDTO userLoginResponseDTO = userService.login(userLoginDTO);
//            add userLoginResponseDTO to response body
            return ResponseEntity.ok(userLoginResponseDTO);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
