package com.bartoszwalter.students.taxes.strategy;

import java.math.BigDecimal;

@FunctionalInterface
public interface TaxDeductibleExpenses {

    BigDecimal calculate(BigDecimal incomeAfterSocialSecurity);
}
