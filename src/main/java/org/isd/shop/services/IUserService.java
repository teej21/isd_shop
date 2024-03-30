package org.isd.shop.services;

import org.isd.shop.responses.user.UserLoginResponse;
import org.isd.shop.responses.user.UserSignupResponse;

public interface IUserService {

    UserSignupResponse registerNewCustomer(
            String fullName,
            String email,
            String phoneNumber,
            String password,
            String gender,
            String role
    ) throws Exception;

    UserLoginResponse login(String username, String password, String role) throws Exception;
}
