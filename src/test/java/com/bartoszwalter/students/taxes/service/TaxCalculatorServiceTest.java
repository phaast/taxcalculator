package com.bartoszwalter.students.taxes.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TaxCalculatorServiceTest {

    private static final BigDecimal INCOME = new BigDecimal("1000.00");

    private final TaxCalculatorService service = new TaxCalculatorService(List.of(
            new EmploymentTaxStrategy(),
            new CivilContractTaxStrategy()));

    @ParameterizedTest
    @EnumSource(ContractType.class)
    void delegatesToStrategyOfRequestedContractType(ContractType contractType) {
        assertThat(service.calculate(contractType, INCOME).getContractType()).isEqualTo(contractType);
    }

    @Test
    void rejectsNegativeIncome() {
        assertThatThrownBy(() -> service.calculate(ContractType.EMPLOYMENT, new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsMissingIncome() {
        assertThatThrownBy(() -> service.calculate(ContractType.EMPLOYMENT, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsContractTypeWithoutStrategy() {
        TaxCalculatorService employmentOnlyService = new TaxCalculatorService(List.of(new EmploymentTaxStrategy()));

        assertThatThrownBy(() -> employmentOnlyService.calculate(ContractType.CIVIL, INCOME))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
