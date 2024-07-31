package com.retail.store.discount.repository;

import com.retail.store.discount.model.Bill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BillRepository extends MongoRepository<Bill, Long> {
}
