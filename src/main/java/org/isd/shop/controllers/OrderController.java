package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.responses.common.ErrorResultResponse;
import org.isd.shop.services.orderDetail.IOrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderDetailService orderService;
    private final Utils utils;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(orderService.createOrder(userId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getOrderById(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(orderService.getOrderByUserId(userId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

}
