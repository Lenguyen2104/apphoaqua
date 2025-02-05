package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.withdraw.CreateWithdrawRequest;
import com.security.vinclub.entity.Deposit;
import com.security.vinclub.entity.Withdraw;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.repository.WithdrawRepository;
import com.security.vinclub.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createWithdrawTransaction(CreateWithdrawRequest request) {
        var isUserExisted = userRepository.existsById(request.getUserId());

        if (!isUserExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
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

        BigDecimal newTotalAmount = user.getTotalAmount().subtract(withdraw.getAmount());
        user.setTotalAmount(newTotalAmount);
        user.setLastDepositAmount(withdraw.getAmount());
        user.setLastDepositDate(LocalDateTime.now());

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

        withdraw.setStatus(AppovalStatusEnum.REJECTED);
        withdrawRepository.save(withdraw);

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
}
