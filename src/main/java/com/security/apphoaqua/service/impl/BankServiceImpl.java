package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.common.SecurityContext;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.bank.CreateBankRequest;
import com.security.apphoaqua.entity.Bank;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.BankRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.SUCCESS;
import static com.security.apphoaqua.core.response.ResponseStatus.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createNewBankInfo(CreateBankRequest request) {
        String userId = SecurityContext.getCurrentUserId();

        userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        var bank = bankRepository.findByUserId(userId)
                .orElseGet(() -> new Bank(UUID.randomUUID().toString(), userId));

        updateIfChanged(bank, request);
        bankRepository.save(bank);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, bank);
        return response;
    }

    @Override
    public ResponseBody<Object> getBankInfo() {
        String userId = SecurityContext.getCurrentUserId();

        userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        var bank = bankRepository.findByUserId(userId)
                .orElseGet(() -> new Bank(UUID.randomUUID().toString(), userId));

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, bank);
        return response;
    }

    private void updateIfChanged(Bank bank, CreateBankRequest request) {
        if (!Objects.equals(bank.getPhone(), request.getPhone())) bank.setPhone(request.getPhone());
        if (!Objects.equals(bank.getAccountName(), request.getAccountName())) bank.setAccountName(request.getAccountName());
        if (!Objects.equals(bank.getAccountNumber(), request.getAccountNumber())) bank.setAccountNumber(request.getAccountNumber());
        if (!Objects.equals(bank.getBankName(), request.getBankName())) bank.setBankName(request.getBankName());
        if (!Objects.equals(bank.getAddress(), request.getAddress())) bank.setAddress(request.getAddress());
    }

}
