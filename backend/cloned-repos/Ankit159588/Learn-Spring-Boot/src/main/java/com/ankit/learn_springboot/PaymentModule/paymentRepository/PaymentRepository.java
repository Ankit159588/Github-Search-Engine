package com.ankit.learn_springboot.PaymentModule.paymentRepository;

import com.ankit.learn_springboot.PaymentModule.paymentEntity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
