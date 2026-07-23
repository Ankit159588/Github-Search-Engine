package com.ankit.learn_springboot.OrderModule.orderRepository;

import com.ankit.learn_springboot.OrderModule.orderEntity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}