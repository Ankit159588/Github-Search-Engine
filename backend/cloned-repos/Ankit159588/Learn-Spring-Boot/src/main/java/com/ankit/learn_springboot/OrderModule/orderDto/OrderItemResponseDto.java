package com.ankit.learn_springboot.OrderModule.orderDto;
import java.math.BigDecimal;

public record OrderItemResponseDto(
        Integer productId,
        String productName,
        Integer quantity,
        BigDecimal priceAtPurchase) {
}
