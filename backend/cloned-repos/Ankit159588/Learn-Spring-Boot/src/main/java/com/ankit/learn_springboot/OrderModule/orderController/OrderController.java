package com.ankit.learn_springboot.OrderModule.orderController;

import com.ankit.learn_springboot.OrderModule.orderDto.CreateOrderRequestDto;
import com.ankit.learn_springboot.OrderModule.orderDto.OrderResponseDto;
import com.ankit.learn_springboot.OrderModule.orderService.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody CreateOrderRequestDto dto,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(dto, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(orderService.getOrderById(id, currentUser));
    }

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(orderService.getMyOrders(currentUser));
    }
}