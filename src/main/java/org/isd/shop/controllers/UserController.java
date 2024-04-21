package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.dtos.UserDTO;
import org.isd.shop.dtos.UserLoginDTO;
import org.isd.shop.responses.common.ErrorResultResponse;
import org.isd.shop.services.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final Utils utils;


    @PostMapping("/admin/users")
    public ResponseEntity<?> registerNewUser(
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(utils.handleBindingResultError(bindingResult));
            }
            return ResponseEntity.ok().body(userService.registerNewUser(
                    userDTO.getFullName(),
                    userDTO.getEmail(),
                    userDTO.getPhoneNumber(),
                    userDTO.getPassword(),
                    userDTO.getGender(),
                    userDTO.getRole(),
                    userDTO.getAddress(),
                    userDTO.getDateOfBirth()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResultResponse(e.getMessage()));
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerNewCustomer(
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        if (!userDTO.getRole().equals("CUSTOMER")) {
            return ResponseEntity.badRequest().body(new ErrorResultResponse("Vai trò không hợp lệ"));
        }
        return registerNewUser(userDTO, bindingResult);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginDTO userLoginDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(utils.handleBindingResultError(bindingResult));
            }
            return ResponseEntity.ok(userService.login(
                    userLoginDTO.getUsername(),
                    userLoginDTO.getPassword()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResultResponse(e.getMessage()));
        }
    }


    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok().body(userService.getAllUsers());
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }


    @GetMapping("admin/users/role={role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        try {
            return ResponseEntity.ok().body(userService.getUsersByRole(role));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(userService.getUserById(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }

    }

    @PutMapping("/admin/users/{id}")
    public ResponseEntity<?> updateUserById(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                throw new Exception(utils.handleBindingResultError(bindingResult));
            }
            return ResponseEntity.ok().body(userService.updateUserById(
                    id,
                    userDTO.getFullName(),
                    userDTO.getEmail(),
                    userDTO.getPhoneNumber(),
                    userDTO.getPassword(),
                    userDTO.getGender(),
                    userDTO.getRole(),
                    userDTO.isActive(),
                    userDTO.getDateOfBirth(),
                    userDTO.getAddress()

            ));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(userService.deleteUserById(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }
}
