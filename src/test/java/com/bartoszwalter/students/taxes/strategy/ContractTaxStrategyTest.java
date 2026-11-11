package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContractTaxStrategyTest {

    @Test
    void reportsContractTypeItWasAssembledWith() {
        TaxStrategy strategy = new ContractTaxStrategy(ContractType.CIVIL, income -> BigDecimal.ZERO, BigDecimal.ZERO);

        assertThat(strategy.getContractType()).isEqualTo(ContractType.CIVIL);
        assertThat(strategy.calculate(new BigDecimal("1000.00")).getContractType()).isEqualTo(ContractType.CIVIL);
    }

    @Test
    void passesIncomeAfterSocialSecurityToTaxDeductibleExpenses() {
        List<BigDecimal> receivedIncomes = new ArrayList<>();
        TaxDeductibleExpenses recordingExpenses = income -> {
            receivedIncomes.add(income);
            return BigDecimal.ZERO;
        };

        new ContractTaxStrategy(ContractType.EMPLOYMENT, recordingExpenses, BigDecimal.ZERO)
                .calculate(new BigDecimal("1000.00"));

        assertThat(receivedIncomes).singleElement().satisfies(income -> assertThat(income).isEqualByComparingTo("862.90"));
    }

    @Test
    void usesTaxDeductibleExpensesAndTaxFreeAllowanceItWasAssembledWith() {
        TaxCalculationResult result = new ContractTaxStrategy(
                ContractType.EMPLOYMENT, income -> new BigDecimal("100.00"), new BigDecimal("10.00"))
                .calculate(new BigDecimal("1000.00"));

        assertThat(result.getTaxDeductibleExpenses()).isEqualByComparingTo("100.00");
        assertThat(result.getTaxableBase()).isEqualByComparingTo("762.90");
        assertThat(result.getAdvanceTax()).isEqualByComparingTo("137.34");
        assertThat(result.getTaxFreeAllowance()).isEqualByComparingTo("10.00");
        assertThat(result.getReducedTax()).isEqualByComparingTo("127.34");
    }
}
