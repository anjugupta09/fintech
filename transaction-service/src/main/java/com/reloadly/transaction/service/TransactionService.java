package com.reloadly.transaction.service;

import com.reloadly.transaction.request.FundTransferRequest;
import com.reloadly.transaction.response.FundTransferResponse;

public interface TransactionService {

    FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) throws Exception;

}
