package com.bartoszwalter.students.taxes.strategy;

import static com.bartoszwalter.students.taxes.strategy.TaxAmounts.applyRate;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class PercentageTaxDeductibleExpenses implements TaxDeductibleExpenses {

    private final BigDecimal rate;

    @Override
    public BigDecimal calculate(BigDecimal incomeAfterSocialSecurity) {
        return applyRate(incomeAfterSocialSecurity, rate);
    }
}
