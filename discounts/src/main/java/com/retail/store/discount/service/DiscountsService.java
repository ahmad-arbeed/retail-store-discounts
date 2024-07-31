package com.retail.store.discount.service;

import com.retail.store.discount.dto.BillRequest;
import com.retail.store.discount.dto.BillResponse;

public interface DiscountsService {

    BillResponse netPayableAmount(BillRequest billRequest);
}