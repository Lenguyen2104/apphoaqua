package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.withdraw.CreateWithdrawRequest;
import org.springframework.data.domain.Pageable;

public interface WithdrawService {

    ResponseBody<Object> createWithdrawTransaction(CreateWithdrawRequest request);

    ResponseBody<Object> approveWithdraw(String id);

    ResponseBody<Object> rejectWithdraw(String id);

    ResponseBody<Object> getPendingWithdraws(Pageable pageable);

    ResponseBody<Object> searchPendingWithdraws(String searchText, Pageable pageable);

    ResponseBody<Object> getBankInfoToWithdraw();
}
