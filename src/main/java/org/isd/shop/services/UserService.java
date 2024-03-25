package org.isd.shop.services;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.dtos.UseRegisterDTO;
import org.isd.shop.dtos.UserLoginDTO;
import org.isd.shop.dtos.UserLoginResponseDTO;
import org.isd.shop.entities.Role;
import org.isd.shop.entities.User;
import org.isd.shop.exceptions.DataNotFoundException;
import org.isd.shop.repositories.RoleRepository;
import org.isd.shop.repositories.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UseRegisterDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
//        convert userDTO to user
        User newUser = new User().builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .gender(userDTO.getGender())
                .build();
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new Exception("Role not found"));
        newUser.setRole(role);

//        password process
        String password = userDTO.getPassword();
        String hashedPassword = passwordEncoder.encode(password);
        newUser.setPassword(hashedPassword);
        return userRepository.save(newUser);
    }

    @Override
    public UserLoginResponseDTO login(UserLoginDTO userLoginDTO) throws Exception {
        String username = userLoginDTO.getUsername();
//        check if username is email or phone number
        Optional<User> user = userRepository.findByPhoneNumber(username);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(username);
        }
        if (user.isEmpty()) {
            throw new DataNotFoundException("Email or phone number is incorrect");
        }
        User existedUser = user.get();
        String email = existedUser.getEmail();
        String password = userLoginDTO.getPassword();
//        check password
        if (!passwordEncoder.matches(password, existedUser.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }
//        authenticate password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken);
//        generate token
        String token = jwtTokenUtil.generateToken(existedUser);

        return new UserLoginResponseDTO().builder()
                .token(token)
                .fullName(existedUser.getFullName())
                .roleId(existedUser.getRole().getId())
                .build();
    }
}
