package org.isd.shop.controllers;

import lombok.RequiredArgsConstructor;
import org.isd.shop.components.Utils;
import org.isd.shop.dtos.ConfirmOrderDTO;
import org.isd.shop.dtos.UpdateOrderEmployeeDTO;
import org.isd.shop.services.order.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final Utils utils;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(orderService.createOrder(userId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.ok(orderService.getAllOrders());
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

    @PutMapping("/admin/update-employee")
    public ResponseEntity<?> updateEmployee(@RequestBody UpdateOrderEmployeeDTO updateOrderEmployeeDTO) {
        try {
            Long employeeId = updateOrderEmployeeDTO.getEmployeeId();
            Long orderId = updateOrderEmployeeDTO.getOrderId();
            return ResponseEntity.ok(orderService.updateEmployee(employeeId, orderId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }


    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<?> getAssignedOrder(@PathVariable Long employeeId, @RequestHeader("Authorization") String authHeader) {
        try {
            return ResponseEntity.ok(orderService.getAssignedOrder(authHeader, employeeId));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }


    }


    @PutMapping("/user/{userId}")
    public ResponseEntity<?> confirmOrder(
        @RequestHeader("Authorization") String authHeader,
        @PathVariable Long userId,
        @RequestBody ConfirmOrderDTO confirmOrderDTO) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new Exception("Token không hợp lệ");
            }
            String token = authHeader.substring(7);

            String name = confirmOrderDTO.getName();
            String address = confirmOrderDTO.getAddress();
            String phone = confirmOrderDTO.getPhoneNumber();
            String note = confirmOrderDTO.getNote();
            return ResponseEntity.ok(orderService.confirmOrder(token, userId, name, address, phone, note));
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

    @GetMapping("/admin/{status}")
    public ResponseEntity<?> getOrderByStatusByAdmin(@PathVariable String status) {
        try {
            return ResponseEntity.ok(orderService.getOrderByStatusByAdmin(status));
        } catch (Exception e) {
            return utils.ErrorResponse(e);
        }
    }

}
