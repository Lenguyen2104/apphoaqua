package com.security.vinclub.service.impl;

import com.security.vinclub.core.response.ErrorData;
import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import com.security.vinclub.dto.response.deposit.PendingDepositResponse;
import com.security.vinclub.entity.Deposit;
import com.security.vinclub.enumeration.AppovalStatusEnum;
import com.security.vinclub.exception.ServiceSecurityException;
import com.security.vinclub.repository.DepositRepository;
import com.security.vinclub.repository.UserRepository;
import com.security.vinclub.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.security.vinclub.core.response.ResponseStatus.*;

@Service
@RequiredArgsConstructor
public class DepositServiceImpl implements DepositService {
    private final DepositRepository depositRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseBody<Object> createDepositTransaction(CreateDepositRequest request) {
        var isUserExisted = userRepository.existsById(request.getUserId());

        if (!isUserExisted) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        }
        Deposit deposit = new Deposit();
        deposit.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        deposit.setUserId(request.getUserId());
        deposit.setAccountNumber(request.getAccountNumber());
        deposit.setAccountName(request.getAccountName());
        deposit.setBankName(request.getBankName());
        deposit.setAmount(request.getAmount());
        deposit.setCreatedDate(LocalDateTime.now());
        depositRepository.save(deposit);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, deposit);
        return response;
    }

    @Override
    @Transactional
    public ResponseBody<Object> approveDeposit(String id) {
        Deposit deposit = depositRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(DEPOSIT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, DEPOSIT_NOT_FOUND, errorMapping);
        });

        deposit.setStatus(AppovalStatusEnum.APPROVED);

        var user = userRepository.findById(deposit.getUserId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(USER_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
        });
        BigDecimal currentAmount = user.getTotalAmount();
        if(currentAmount == null) {
            currentAmount = BigDecimal.ZERO;
        }
        BigDecimal newTotalAmount = currentAmount.add(deposit.getAmount());
        user.setTotalAmount(newTotalAmount);
        user.setLastDepositAmount(deposit.getAmount());
        user.setLastDepositDate(LocalDateTime.now());

        depositRepository.save(deposit);
        userRepository.save(user);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, id);
        return response;
    }

    @Override
    @Transactional
    public ResponseBody<Object> rejectDeposit(String id) {
        Deposit deposit = depositRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(DEPOSIT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, DEPOSIT_NOT_FOUND, errorMapping);
        });

        deposit.setStatus(AppovalStatusEnum.REJECTED);
        depositRepository.save(deposit);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, id);
        return response;
    }

    @Override
    public ResponseBody<Object> getPendingDeposits(Pageable pageable) {
        var pendingDeposits = depositRepository.findByStatus(AppovalStatusEnum.PENDING, pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, pendingDeposits);
        return response;
    }

    @Override
    public ResponseBody<Object> searchPendingDeposits(String searchText, Pageable pageable) {
        var pendingDeposits = depositRepository.searchPendingDeposits(searchText.toLowerCase(), pageable);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, pendingDeposits);
        return response;
    }

}
