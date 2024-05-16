package org.isd.shop.repositories;

import org.isd.shop.entities.Order;
import org.isd.shop.entities.User;
import org.isd.shop.enums.Enums;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findByUser(User user);

    Optional<List<Order>> findByStatus(Enums.OrderStatus status);

    Optional<List<Order>> findByUserAndStatus(User user, Enums.OrderStatus orderStatus);

    Optional<List<Order>> findByEmployee(User user);
}
