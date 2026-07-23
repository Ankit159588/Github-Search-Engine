package com.ankit.learn_springboot.PaymentModule.dto;

import java.math.BigDecimal;

public record CreatePaymentResponseDto (
        String razorpayOrderId,
        String razorpayKeyId,
        BigDecimal amount,
        String currency
){
}
