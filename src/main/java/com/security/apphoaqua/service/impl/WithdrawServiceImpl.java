package com.security.apphoaqua.service.impl;

import com.security.apphoaqua.common.SecurityContext;
import com.security.apphoaqua.core.response.ErrorData;
import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.withdraw.CreateWithdrawRequest;
import com.security.apphoaqua.dto.response.bank.BankInfoToWithdrawResponse;
import com.security.apphoaqua.entity.Bank;
import com.security.apphoaqua.entity.User;
import com.security.apphoaqua.entity.Withdraw;
import com.security.apphoaqua.enumeration.AppovalStatusEnum;
import com.security.apphoaqua.exception.ServiceSecurityException;
import com.security.apphoaqua.repository.BankRepository;
import com.security.apphoaqua.repository.UserRepository;
import com.security.apphoaqua.repository.WithdrawRepository;
import com.security.apphoaqua.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.security.apphoaqua.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;

    @Override
    public ResponseBody<Object> createWithdrawTransaction(CreateWithdrawRequest request) {
        String userId = SecurityContext.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        if (user.getTotalAmount().compareTo(request.getAmount()) < 0) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(INSUFFICIENT_BALANCE.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, INSUFFICIENT_BALANCE, errorMapping);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        withdraw.setUserId(request.getUserId());
        withdraw.setAccountNumber(request.getAccountNumber());
        withdraw.setAccountName(request.getAccountName());
        withdraw.setBankName(request.getBankName());
        withdraw.setAmount(request.getAmount());
        withdraw.setCreatedDate(LocalDateTime.now());
        withdrawRepository.save(withdraw);

        user.setTotalAmount(user.getTotalAmount().subtract(request.getAmount()));
        userRepository.save(user);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, withdraw);
        return response;
    }

    @Override
    public ResponseBody<Object> approveWithdraw(String id) {
        Withdraw withdraw = withdrawRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(WITHDRAW_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, WITHDRAW_NOT_FOUND, errorMapping);
        });

        withdraw.setStatus(AppovalStatusEnum.APPROVED);

        var user = userRepository.findById(withdraw.getUserId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        withdrawRepository.save(withdraw);
        userRepository.save(user);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, id);
        return response;
    }

    @Override
    @Transactional
    public ResponseBody<Object> rejectWithdraw(String id) {
        Withdraw withdraw = withdrawRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(WITHDRAW_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, WITHDRAW_NOT_FOUND, errorMapping);
        });

        var user = userRepository.findById(withdraw.getUserId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        BigDecimal newTotalAmount = user.getTotalAmount().add(withdraw.getAmount());
        user.setTotalAmount(newTotalAmount);
        withdraw.setStatus(AppovalStatusEnum.REJECTED);
        withdrawRepository.save(withdraw);
        userRepository.save(user);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, id);
        return response;
    }

    @Override
    public ResponseBody<Object> getPendingWithdraws(Pageable pageable) {
        var pendingWithdraws = withdrawRepository.findByStatus(AppovalStatusEnum.PENDING, pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, pendingWithdraws);
        return response;
    }

    @Override
    public ResponseBody<Object> searchPendingWithdraws(String searchText, Pageable pageable) {
        var pendingDeposits = withdrawRepository.searchPendingWithdraws(searchText.toLowerCase(), pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, pendingDeposits);
        return response;
    }

    @Override
    public ResponseBody<Object> getBankInfoToWithdraw() {
        String userId = SecurityContext.getCurrentUserId();
        var user = userRepository.findById(userId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });

        var bank = bankRepository.findByUserId(userId)
                .orElseThrow(() -> new ServiceSecurityException(HttpStatus.OK, BANK_NOT_FOUND));

        BankInfoToWithdrawResponse info = BankInfoToWithdrawResponse.builder()
                .bankId(bank.getId())
                .userId(userId)
                .accountName(bank.getAccountName())
                .accountNumber(bank.getAccountNumber())
                .bankName(bank.getBankName())
                .totalAmount(user.getTotalAmount())
                .build();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, info);
        return response;
    }
}
