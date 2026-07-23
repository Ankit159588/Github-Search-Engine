package com.ankit.learn_springboot.OrderModule.orderService.orderServiceImpl;

import com.ankit.learn_springboot.OrderModule.orderDto.*;
import com.ankit.learn_springboot.OrderModule.orderEntity.Order;
import com.ankit.learn_springboot.OrderModule.orderEntity.OrderItem;
import com.ankit.learn_springboot.OrderModule.orderEntity.OrderStatus;
import com.ankit.learn_springboot.OrderModule.orderRepository.OrderRepository;
import com.ankit.learn_springboot.OrderModule.orderService.OrderService;
import com.ankit.learn_springboot.ProductModule.productEntity.Product;
import com.ankit.learn_springboot.ProductModule.productRepository.ProductRepository;
import com.ankit.learn_springboot.UserModule.user.User;
import com.ankit.learn_springboot.UserModule.userRepo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto dto, UserDetails currentUser) {

        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> orderItems = dto.items().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.productId()));

            return OrderItem.builder()
                    .product(product)
                    .quantity(itemDto.quantity())
                    .priceAtPurchase(product.getPrice())   // snapshot current price
                    .build();
        }).toList();

        BigDecimal total = orderItems.stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(total)
                .build();

        orderItems.forEach(item -> item.setOrder(order));
        order.setItems(orderItems);

        Order saved = orderRepository.save(order);
        return mapToResponseDto(saved);
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId, UserDetails currentUser) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUsername().equals(currentUser.getUsername())) {
            throw new RuntimeException("Not authorized to view this order");
        }

        return mapToResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getMyOrders(UserDetails currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    private OrderResponseDto mapToResponseDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                )).toList();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getUsername(),
                itemDtos,
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}