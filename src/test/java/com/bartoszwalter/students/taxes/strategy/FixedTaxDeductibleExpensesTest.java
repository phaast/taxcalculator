package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FixedTaxDeductibleExpensesTest {

    private final TaxDeductibleExpenses expenses = new FixedTaxDeductibleExpenses(new BigDecimal("111.25"));

    @ParameterizedTest
    @ValueSource(strings = {"0.00", "862.90", "4314.50"})
    void returnsSameAmountRegardlessOfIncome(String incomeAfterSocialSecurity) {
        assertThat(expenses.calculate(new BigDecimal(incomeAfterSocialSecurity))).isEqualByComparingTo("111.25");
    }
}
