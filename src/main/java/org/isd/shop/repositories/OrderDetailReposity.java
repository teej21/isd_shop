package org.isd.shop.repositories;

import org.isd.shop.entities.Order;
import org.isd.shop.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailReposity extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);
}
