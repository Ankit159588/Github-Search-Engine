package com.ankit.learn_springboot.OrderModule.orderDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDto(@NotNull Integer productId,
                                  @Min(1) Integer quantity) {
}
