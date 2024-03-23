package org.isd.shop.services;

import org.isd.shop.dtos.UserDTO;
import org.isd.shop.entities.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    String login(String email, String password, Long roleId) throws Exception;
}
