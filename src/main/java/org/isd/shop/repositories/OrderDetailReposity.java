package org.isd.shop.repositories;

import org.isd.shop.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailReposity extends JpaRepository<OrderDetail, Long> {
}
