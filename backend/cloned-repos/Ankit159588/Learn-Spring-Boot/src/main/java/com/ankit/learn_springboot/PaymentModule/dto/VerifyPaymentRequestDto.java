package com.ankit.learn_springboot.PaymentModule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPaymentRequestDto {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
