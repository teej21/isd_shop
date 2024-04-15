package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.dtos.OrderDetailDTO;
import org.isd.shop.services.orderDetail.IOrderDetailService;
import org.isd.shop.services.orderDetail.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    private final Utils utils;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        try{
            return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderDetailService.getOrderDetailById(id));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Validated @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
            return ResponseEntity.ok(orderDetailService.createOrderDetail(orderDetailDTO.getUserId(), orderDetailDTO.getProductId()));
        } catch (Exception e) {
            return utils.ErrorResponse(e);

        }
    }

}
