package org.isd.shop.services;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.JwtTokenUtil;
import org.isd.shop.dtos.UserDTO;
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
    public User createUser(UserDTO userDTO) throws Exception {
        String email = userDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new DataIntegrityViolationException("Email already exists");
        }
//        convert userDTO to user
        User newUser = new User().builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .dateOfBirth(userDTO.getDateOfBirth())
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
    public String login(String email, String password, Long roleId) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new DataNotFoundException("Cannot find user with email: " + email);
        }
        User existedUser = user.get();
//        check password
        if (!passwordEncoder.matches(password, existedUser.getPassword())) {
            throw new BadCredentialsException("Password is incorrect");
        }
//        authenticate password
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existedUser);
    }
}
