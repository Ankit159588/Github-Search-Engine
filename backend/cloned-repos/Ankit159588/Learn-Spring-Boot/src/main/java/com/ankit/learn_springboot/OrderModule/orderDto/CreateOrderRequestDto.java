package com.ankit.learn_springboot.OrderModule.orderDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequestDto(
        @NotEmpty
        @Valid List<OrderItemRequestDto> items
) {
}
