package org.isd.shop.services.user;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.components.Utils;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.isd.shop.repositories.TokenRepository;
import org.isd.shop.repositories.UserRepository;
import org.isd.shop.responses.common.ResultResponse;
import org.isd.shop.responses.user.RefreshTokenResponse;
import org.isd.shop.responses.user.UserLoginResponse;
import org.isd.shop.responses.user.UserResponse;
import org.isd.shop.responses.user.UserSignupResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    public UserLoginResponse login(String username, String password, String role) throws Exception {
        try {
            if (!utils.isValidRole(role)) {
                throw new Exception("Not found role " + role);
            }
            boolean isEmail = utils.isValidEmail(username);
            boolean isPhoneNumber = utils.isValidPhoneNumber(username);
            if (!isEmail && !isPhoneNumber) {
                throw new Exception("Email hoặc số điện thoại không hợp lệ");
            }
            Optional<User> userOptional = isEmail ? userRepository.findByEmail(username) : userRepository.findByPhoneNumber(username);
            if (userOptional.isEmpty()) {
                throw new Exception("Số điện thoại hoặc email không đúng. Vui lòng thử lại.");
            }
            User user = userOptional.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new Exception("Mật khẩu không đúng. Vui lòng thử lại.");
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    password);
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            RefreshTokenResponse refreshTokenResponse = jwtTokenUtil.generateTokens(user.getEmail());
            return UserLoginResponse.builder()
                    .fullName(user.getFullName())
                    .tokens(refreshTokenResponse)
                    .role(user.getRole().toString())
                    .build();
        } catch (Exception e) {
            throw new Exception(e.getMessage());

        }
    }

    @Override
    public UserSignupResponse registerNewUser(String fullName,
                                              String email,
                                              String phoneNumber,
                                              String password,
                                              String gender,
                                              String role,
                                              String address,
                                              Date dateOfBirth
    ) throws Exception {
        try {
            validateUser(role, gender, email, phoneNumber);
            //          register for customer
            User user = User.builder()
                    .fullName(fullName)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .password(passwordEncoder.encode(password))
                    .gender(Enums.Gender.valueOf(gender.toUpperCase()))
                    .role(Enums.Role.valueOf(role.toUpperCase()))
                    .dateOfBirth(dateOfBirth)
                    .address(address)
                    .build();

            userRepository.save(user);
            return new UserSignupResponse("Đăng kí thành công");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public List<UserResponse> getAllUsers() {
        Optional<List<User>> users = Optional.of(userRepository.findAll());
        if (users.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng. Vui lòng thử lại.");
        }

        return users.get().stream().map(UserResponse::fromUser).toList();
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng. Vui lòng thử lại.");
        }
        return UserResponse.fromUser(user.get());
    }

    @Override
    public User updateUserById(Long id, String fullName, String email, String phoneNumber, String password, String gender, String role, boolean active, Date dateOfBirth, String address) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new RuntimeException("Không tìm thấy người dùng. Vui lòng thử lại.");
            }
            User user = userOptional.get();
            validateUser(role, gender, email, phoneNumber);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(passwordEncoder.encode(password));
            user.setGender(Enums.Gender.valueOf(gender));
            user.setRole(Enums.Role.valueOf(role));
            user.setActive(active);
            user.setDateOfBirth(dateOfBirth);
            user.setAddress(address);
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ResultResponse deleteUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng. Vui lòng thử lại.");
        }
        userRepository.deleteById(id);
        return new ResultResponse("Xóa người dùng thành công");
    }

    @Override
    public List<UserResponse> getUsersByRole(String role) {
        if (!utils.isValidRole(role)) {
            throw new RuntimeException("Vai trò không hợp lệ");
        }
        Optional<List<User>> users = userRepository.findByRole(Enums.Role.valueOf(role.toUpperCase()));

        return users.get().stream().map(UserResponse::fromUser).toList();
    }

    @Override
    public boolean checkUserExist(Long userId) {
        return userRepository.existsById(userId);
    }

    private void validateUser(
            String role,
            String gender,
            String email,
            String phoneNumber
    ) throws Exception {
        if (!utils.isValidRole(role)) {
            throw new Exception("Not found role " + role);
        }

        if (!utils.isValidGender(gender)) {
            throw new Exception("Giới tính không hợp lệ");
        }

        if (!utils.isValidEmail(email)) {
            throw new Exception("Email không hợp lệ");
        }

        if (!utils.isValidPhoneNumber(phoneNumber)) {
            throw new Exception("Số điện thoại không hợp lệ");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            throw new Exception("Email đã được sử dụng để đăng ký. Vui lòng sử dụng email khác.");
        }
        Optional<User> userOptional1 = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional1.isPresent()) {
            throw new Exception("Số điện thoại đã được sử dụng để đăng ký. Vui lòng sử dụng số điện thoại khác.");
        }
    }
}

