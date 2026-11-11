package com.bartoszwalter.students.taxes.strategy;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FixedTaxDeductibleExpenses implements TaxDeductibleExpenses {

    private final BigDecimal amount;

    @Override
    public BigDecimal calculate(BigDecimal incomeAfterSocialSecurity) {
        return amount;
    }
}
