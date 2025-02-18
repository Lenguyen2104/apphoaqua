package com.security.apphoaqua.controller;

import com.security.apphoaqua.dto.request.bank.CreateBankRequest;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.service.BankService;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;
    private final Validator validator;

    @PostMapping("/user/bank")
    public ResponseEntity<Object> postNewBankInfo(@RequestBody CreateBankRequest request) {
        this.validateRequest(request);
        return ResponseEntity.ok(bankService.createNewBankInfo(request));
    }

    @GetMapping("/user/bank")
    public ResponseEntity<Object> getBankInfo() {
        return ResponseEntity.ok(bankService.getBankInfo());
    }

    private <T> void validateRequest(T request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ServiceSecurityException(violations);
    }
}
