package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmploymentTaxStrategyTest {

    private final TaxStrategy strategy = new EmploymentTaxStrategy();

    @Test
    void calculatesFullBreakdownForEmploymentContract() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("1000.00"));

        assertThat(result.getContractType()).isEqualTo(ContractType.EMPLOYMENT);
        assertThat(result.getGrossIncome()).isEqualByComparingTo("1000.00");
        assertThat(result.getPensionContribution()).isEqualByComparingTo("97.60");
        assertThat(result.getDisabilityContribution()).isEqualByComparingTo("15.00");
        assertThat(result.getSicknessContribution()).isEqualByComparingTo("24.50");
        assertThat(result.getTotalSocialSecurity()).isEqualByComparingTo("137.10");
        assertThat(result.getHealthContributionBase()).isEqualByComparingTo("862.90");
        assertThat(result.getHealthContribution()).isEqualByComparingTo("77.66");
        assertThat(result.getTaxDeductibleHealthContribution()).isEqualByComparingTo("66.87");
        assertThat(result.getTaxDeductibleExpenses()).isEqualByComparingTo("111.25");
        assertThat(result.getTaxableBase()).isEqualByComparingTo("751.65");
        assertThat(result.getRoundedTaxableBase()).isEqualByComparingTo("752");
        assertThat(result.getAdvanceTax()).isEqualByComparingTo("135.36");
        assertThat(result.getTaxFreeAllowance()).isEqualByComparingTo("46.33");
        assertThat(result.getReducedTax()).isEqualByComparingTo("89.03");
        assertThat(result.getAdvanceTaxPaid()).isEqualByComparingTo("22.16");
        assertThat(result.getRoundedAdvanceTaxPaid()).isEqualByComparingTo("22");
        assertThat(result.getNetIncome()).isEqualByComparingTo("763.24");
    }

    @ParameterizedTest
    @CsvSource({
            "1000.00, 763.24",
            "5000.00, 3550.19",
            "1000.98, 764.01",
            "300.00, 235.57",
            "100.00, 78.52",
            "0.00, 0.00"
    })
    void calculatesNetIncome(BigDecimal grossIncome, BigDecimal expectedNetIncome) {
        assertThat(strategy.calculate(grossIncome).getNetIncome()).isEqualByComparingTo(expectedNetIncome);
    }

    @Test
    void roundsContributionsHalfUpToFullGrosz() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("5000.00"));

        assertThat(result.getHealthContribution()).isEqualByComparingTo("388.31");
    }

    @Test
    void roundsTaxableBaseHalfUpToFullZloty() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("1000.98"));

        assertThat(result.getTaxableBase()).isEqualByComparingTo("752.50");
        assertThat(result.getRoundedTaxableBase()).isEqualByComparingTo("753");
    }

    @Test
    void doesNotProduceNegativeTaxWhenTaxFreeAllowanceExceedsTax() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("300.00"));

        assertThat(result.getAdvanceTax()).isEqualByComparingTo("26.64");
        assertThat(result.getReducedTax()).isEqualByComparingTo("0");
        assertThat(result.getAdvanceTaxPaid()).isEqualByComparingTo("0");
    }

    @Test
    void doesNotProduceNegativeTaxableBaseWhenExpensesExceedIncome() {
        TaxCalculationResult result = strategy.calculate(new BigDecimal("100.00"));

        assertThat(result.getTaxableBase()).isEqualByComparingTo("0");
        assertThat(result.getAdvanceTax()).isEqualByComparingTo("0");
        assertThat(result.getRoundedAdvanceTaxPaid()).isEqualByComparingTo("0");
    }
}
