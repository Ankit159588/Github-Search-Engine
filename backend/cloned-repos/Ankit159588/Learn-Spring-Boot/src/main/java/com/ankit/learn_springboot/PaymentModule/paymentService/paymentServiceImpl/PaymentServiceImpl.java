package com.ankit.learn_springboot.PaymentModule.paymentService.paymentServiceImpl;

import com.ankit.learn_springboot.OrderModule.orderEntity.Order;
import com.ankit.learn_springboot.OrderModule.orderEntity.OrderStatus;
import com.ankit.learn_springboot.OrderModule.orderRepository.OrderRepository;
import com.ankit.learn_springboot.PaymentModule.dto.CreatePaymentResponseDto;
import com.ankit.learn_springboot.PaymentModule.dto.VerifyPaymentRequestDto;
import com.ankit.learn_springboot.PaymentModule.paymentEntity.Payment;
import com.ankit.learn_springboot.PaymentModule.paymentEntity.PaymentStatus;
import com.ankit.learn_springboot.PaymentModule.paymentRepository.PaymentRepository;
import com.ankit.learn_springboot.PaymentModule.paymentService.PaymentService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;


    @Override
    public CreatePaymentResponseDto createPaymentOrder(Long orderId, UserDetails userDetails) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if(!order.getUser().getUsername().equals(userDetails.getUsername())) {
            throw new RuntimeException("Unauthorized access to order");
        }

        try{
            int amountInPaise = order.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue();

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcpt_" + order.getId());

            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);

            Payment payment = Payment.builder()
                    .order(order)
                    .razorpayOrderId(razorpayOrder.get("id"))
                    .amount(order.getTotalAmount())
                    .status(PaymentStatus.CREATED)
                    .build();

            paymentRepository.save(payment);

            return new CreatePaymentResponseDto(
                    razorpayOrder.get("id"),
                    razorpayKeyId,
                    order.getTotalAmount(),
                    "INR"
            );


        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage());
        }

    }


    @Override
    @Transactional
    public boolean verifyPayment(VerifyPaymentRequestDto dto) {

        try {

            JSONObject options = new JSONObject();

            options.put("razorpay_order_id", dto.getRazorpayOrderId());
            options.put("razorpay_payment_id", dto.getRazorpayPaymentId());
            options.put("razorpay_signature", dto.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(
                    options,
                    razorpayKeySecret
            );

            if (!isValid) {
                return false;
            }

            Payment payment = paymentRepository
                    .findByRazorpayOrderId(dto.getRazorpayOrderId())
                    .orElseThrow(() ->
                            new RuntimeException("Payment not found"));

            payment.setRazorpayPaymentId(
                    dto.getRazorpayPaymentId());

            payment.setRazorpaySignature(
                    dto.getRazorpaySignature());

            payment.setStatus(PaymentStatus.SUCCESS);

            paymentRepository.save(payment);

            // Propagate the successful payment to the order itself.
            // Without this, the Order stays stuck at PENDING forever
            // even though the payment succeeded.
            Order order = payment.getOrder();
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);

            return true;

        } catch (Exception e) {
            return false;
        }
    }

}