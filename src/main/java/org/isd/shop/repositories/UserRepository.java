package org.isd.shop.repositories;


import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String Email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<List<User>> findByRole(Enums.Role role);
}

