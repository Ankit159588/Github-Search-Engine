package com.ankit.learn_springboot.PaymentModule.paymentController;

import com.ankit.learn_springboot.PaymentModule.dto.CreatePaymentResponseDto;
import com.ankit.learn_springboot.PaymentModule.dto.PaymentVerificationRequest;
import com.ankit.learn_springboot.PaymentModule.dto.VerifyPaymentRequestDto;
import com.ankit.learn_springboot.PaymentModule.paymentService.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<CreatePaymentResponseDto> createPayment(
            @PathVariable Long orderId,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(paymentService.createPaymentOrder(orderId, currentUser));
    }


    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(
            @Valid @RequestBody VerifyPaymentRequestDto dto) {

        boolean verified = paymentService.verifyPayment(dto);

        if (verified) {
            return ResponseEntity.ok("Payment verified successfully");
        }

        return ResponseEntity.badRequest()
                .body("Invalid payment signature");
    }

}
