package com.ankit.learn_springboot.OrderModule.orderService;

import com.ankit.learn_springboot.OrderModule.orderDto.CreateOrderRequestDto;
import com.ankit.learn_springboot.OrderModule.orderDto.OrderResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto dto, UserDetails currentUser);
    OrderResponseDto getOrderById(Long orderId, UserDetails currentUser);
    List<OrderResponseDto> getMyOrders(UserDetails currentUser);
}
