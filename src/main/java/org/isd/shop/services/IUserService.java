package org.isd.shop.services;

import org.isd.shop.dtos.UseRegisterDTO;
import org.isd.shop.dtos.UserLoginDTO;
import org.isd.shop.dtos.UserLoginResponseDTO;
import org.isd.shop.entities.User;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IUserService {
    User createUser(UseRegisterDTO userDTO) throws Exception;
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO) throws Exception;
}
