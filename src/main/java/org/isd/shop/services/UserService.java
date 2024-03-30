package org.isd.shop.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.components.Utils;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.TokenRepository;
import org.isd.shop.repositories.UserRepository;
import org.isd.shop.responses.user.RefreshTokenResponse;
import org.isd.shop.responses.user.UserLoginResponse;
import org.isd.shop.responses.user.UserSignupResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final Utils utils;

    @Override
    public UserSignupResponse registerNewCustomer(String fullName, String email, String phoneNumber, String password, String gender, String role) throws Exception {
        try {
            if (!utils.isValidRole(role)) {
                throw new Exception("Not found role " + role);
            }

            if (!utils.isValidGender(gender)) {
                throw new Exception("Gender is invalid");
            }

            if (!utils.isValidEmail(email)) {
                throw new Exception("Invalid email");
            }

            if (!utils.isValidPhoneNumber(phoneNumber)) {
                throw new Exception("Invalid phone number");
            }

            if (!role.equals(Enums.Role.CUSTOMER.toString())) {
                throw new Exception("Invalid role");
            }
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                throw new Exception("User email already exists");
            }
            Optional<User> userOptional1 = userRepository.findByPhoneNumber(phoneNumber);
            if (userOptional1.isPresent()) {
                throw new Exception("User phone number already exists");
            }
            //          register for customer
            User user = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .password(passwordEncoder.encode(password))
                    .gender(Enums.Gender.valueOf(gender.toUpperCase()))
                    .role(Enums.Role.CUSTOMER)
                    .build();

            userRepository.save(user);
            return new UserSignupResponse("User registered successfully");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    public UserLoginResponse login(String username, String password, String role) throws Exception {
        try {
            if (!utils.isValidRole(role)) {
                throw new Exception("Not found role " + role);
            }
            boolean isEmail = utils.isValidEmail(username);
            boolean isPhoneNumber = utils.isValidPhoneNumber(username);
            if (!isEmail && !isPhoneNumber) {
                throw new Exception("Invalid email or phone number");
            }
            Optional<User> userOptional = isEmail ? userRepository.findByEmail(username) : userRepository.findByPhoneNumber(username);
            if (userOptional.isEmpty()) {
                throw new Exception("User not found");
            }
            User user = userOptional.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new Exception("Invalid password");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    password);
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            RefreshTokenResponse refreshTokenResponse = jwtTokenUtil.generateTokens(user.getEmail());
            return UserLoginResponse.builder()
                    .fullName(user.getFullName())
                    .tokens(refreshTokenResponse)
                    .build();
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        }
    }


}

