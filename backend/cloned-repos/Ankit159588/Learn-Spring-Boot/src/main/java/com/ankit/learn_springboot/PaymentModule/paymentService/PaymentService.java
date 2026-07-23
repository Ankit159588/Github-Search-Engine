package com.ankit.learn_springboot.PaymentModule.paymentService;

import com.ankit.learn_springboot.PaymentModule.dto.CreatePaymentResponseDto;
import com.ankit.learn_springboot.PaymentModule.dto.PaymentVerificationRequest;
import com.ankit.learn_springboot.PaymentModule.dto.VerifyPaymentRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface PaymentService {
    CreatePaymentResponseDto createPaymentOrder(Long orderId, UserDetails userDetails);
    boolean verifyPayment(VerifyPaymentRequestDto dto);

}
