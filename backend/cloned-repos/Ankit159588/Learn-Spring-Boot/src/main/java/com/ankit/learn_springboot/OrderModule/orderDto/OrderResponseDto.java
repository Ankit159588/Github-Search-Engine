package com.ankit.learn_springboot.OrderModule.orderDto;

import com.ankit.learn_springboot.OrderModule.orderEntity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        Long id,
        String username,
        List<OrderItemResponseDto> items,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
