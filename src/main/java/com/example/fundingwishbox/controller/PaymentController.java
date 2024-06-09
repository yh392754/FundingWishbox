package com.example.fundingwishbox.controller;

import com.example.fundingwishbox.service.IamportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IamportService iamportService;

    public PaymentController(IamportService iamportService) {
        this.iamportService = iamportService;
    }

    @GetMapping("/{impUid}")
    public ResponseEntity<Map<String, Object>> getPaymentData(@PathVariable String impUid) {
        Map<String, Object> paymentData = iamportService.getPaymentData(impUid);
        return ResponseEntity.ok(paymentData);
    }
}
