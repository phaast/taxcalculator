package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CivilContractTaxStrategyTest {

    private final TaxStrategy strategy = new CivilContractTaxStrategy();

    @Test
    void calculatesFullBreakdownForCivilContract() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("1000.00"));

        assertThat(result.getContractType()).isEqualTo(ContractType.CIVIL);
        assertThat(result.getTotalSocialSecurity()).isEqualByComparingTo("137.10");
        assertThat(result.getHealthContributionBase()).isEqualByComparingTo("862.90");
        assertThat(result.getHealthContribution()).isEqualByComparingTo("77.66");
        assertThat(result.getTaxDeductibleHealthContribution()).isEqualByComparingTo("66.87");
        assertThat(result.getTaxDeductibleExpenses()).isEqualByComparingTo("172.58");
        assertThat(result.getTaxableBase()).isEqualByComparingTo("690.32");
        assertThat(result.getRoundedTaxableBase()).isEqualByComparingTo("690");
        assertThat(result.getAdvanceTax()).isEqualByComparingTo("124.20");
        assertThat(result.getTaxFreeAllowance()).isEqualByComparingTo("0.00");
        assertThat(result.getReducedTax()).isEqualByComparingTo("124.20");
        assertThat(result.getAdvanceTaxPaid()).isEqualByComparingTo("57.33");
        assertThat(result.getRoundedAdvanceTaxPaid()).isEqualByComparingTo("57");
        assertThat(result.getNetIncome()).isEqualByComparingTo("728.24");
    }

    @Test
    void calculatesTaxDeductibleExpensesFromIncomeAfterSocialSecurity() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("5000.00"));

        assertThat(result.getHealthContributionBase()).isEqualByComparingTo("4314.50");
        assertThat(result.getTaxDeductibleExpenses()).isEqualByComparingTo("862.90");
    }

    @ParameterizedTest
    @CsvSource({
            "1000.00, 728.24",
            "5000.00, 3639.19",
            "0.00, 0.00"
    })
    void calculatesNetIncome(BigDecimal grossIncome, BigDecimal expectedNetIncome) {
        assertThat(strategy.calculate(grossIncome).getNetIncome()).isEqualByComparingTo(expectedNetIncome);
    }
}
