package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.deposit.CreateDepositRequest;
import org.springframework.data.domain.Pageable;

public interface DepositService {

    ResponseBody<Object> createDepositTransaction(CreateDepositRequest request);

    ResponseBody<Object> approveDeposit(String id);

    ResponseBody<Object> rejectDeposit(String id);

    ResponseBody<Object> getPendingDeposits(Pageable pageable);

    ResponseBody<Object> searchPendingDeposits(String searchText, Pageable pageable);
}
