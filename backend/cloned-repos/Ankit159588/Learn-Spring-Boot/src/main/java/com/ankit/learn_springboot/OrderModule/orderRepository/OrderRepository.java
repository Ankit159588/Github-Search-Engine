package com.ankit.learn_springboot.OrderModule.orderRepository;

import com.ankit.learn_springboot.OrderModule.orderEntity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
