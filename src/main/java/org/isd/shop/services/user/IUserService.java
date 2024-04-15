package org.isd.shop.services.user;

import org.isd.shop.entities.User;
import org.isd.shop.responses.common.ResultResponse;
import org.isd.shop.responses.user.UserLoginResponse;
import org.isd.shop.responses.user.UserResponse;
import org.isd.shop.responses.user.UserSignupResponse;

import java.util.Date;
import java.util.List;

public interface IUserService {


    UserLoginResponse login(String username, String password, String role) throws Exception;



    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    User updateUserById(Long id, String fullName, String email, String phoneNumber, String password, String gender, String role, boolean active, Date dateOfBirth, String address);

    ResultResponse deleteUserById(Long id);

    List<UserResponse> getUsersByRole(String role);

    boolean checkUserExist(Long userId);

    UserSignupResponse registerNewUser(String fullName, String email, String phoneNumber, String password, String gender, String role, String address, Date dateOfBirth) throws Exception;
}
