package com.retail.store.discount.service.discount.factory;

import com.retail.store.discount.constant.UserType;
import com.retail.store.discount.service.discount.Discount;
import com.retail.store.discount.service.discount.strategy.AffiliatePercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.CustomerPercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.EmployeePercentageDiscountStrategy;
import com.retail.store.discount.service.discount.strategy.NoPercentageDiscountStrategy;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PercentageDiscountStrategyFactory {

    private final EmployeePercentageDiscountStrategy employeeDiscount;
    private final AffiliatePercentageDiscountStrategy affiliateDiscount;
    private final CustomerPercentageDiscountStrategy customerDiscount;
    private final NoPercentageDiscountStrategy noDiscount;

    public Discount getPercentageDiscountStrategy(UserType userType, Instant createdDate) {
        return switch (userType) {
            case EMPLOYEE -> employeeDiscount;
            case AFFILIATE -> affiliateDiscount;
            case CUSTOMER -> {
                if (ChronoUnit.DAYS.between(createdDate, Instant.now()) / 365 > 2) {
                    yield customerDiscount;
                }
                yield noDiscount;
            }
        };
    }
}