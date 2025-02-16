package com.security.apphoaqua.service;

import com.security.apphoaqua.core.response.ResponseBody;
import com.security.apphoaqua.dto.request.bank.CreateBankRequest;

public interface BankService {
    ResponseBody<Object> createNewBankInfo(CreateBankRequest request);

    ResponseBody<Object> getBankInfo();
}
