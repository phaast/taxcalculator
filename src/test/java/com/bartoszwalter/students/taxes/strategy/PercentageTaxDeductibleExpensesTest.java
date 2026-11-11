package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PercentageTaxDeductibleExpensesTest {

    private final TaxDeductibleExpenses expenses = new PercentageTaxDeductibleExpenses(new BigDecimal("0.20"));

    @ParameterizedTest
    @CsvSource({"862.90, 172.58", "4314.50, 862.90", "0.00, 0.00", "0.03, 0.01"})
    void returnsPercentageOfIncomeRoundedToFullGrosz(String incomeAfterSocialSecurity, String expectedExpenses) {
        assertThat(expenses.calculate(new BigDecimal(incomeAfterSocialSecurity))).isEqualByComparingTo(expectedExpenses);
    }
}
