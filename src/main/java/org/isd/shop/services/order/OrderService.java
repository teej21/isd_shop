package org.isd.shop.services.order;

import lombok.RequiredArgsConstructor;
import org.isd.shop.repositories.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
}
