package com.security.vinclub.service;

import com.security.vinclub.core.response.ResponseBody;
import com.security.vinclub.dto.request.deposit.CreateDepositRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface DepositService {

    ResponseBody<Object> createDepositTransaction(CreateDepositRequest request);

    ResponseBody<Object> approveDeposit(String id);

    ResponseBody<Object> rejectDeposit(String id);

    ResponseBody<Object> getPendingDeposits(Pageable pageable);

    ResponseBody<Object> searchPendingDeposits(String searchText, Pageable pageable);
}
