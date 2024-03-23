package org.isd.shop.repositories;


import org.isd.shop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String Email);
    Optional<User> findByEmail(String Email);
    //SELECT * FROM users WHERE phoneNumber=?
}

