package com.bartoszwalter.students.taxes.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CivilContractReportGeneratorTest {

    private final TaxReportGenerator generator = new CivilContractReportGenerator();

    @Test
    void generatesCivilContractReport() {
        assertThat(generator.generate(civilContractResult()).getLines()).containsExactly(
                "income 1000.00",
                "Social security tax = 97.60",
                "Disability social security tax = 15.00",
                "Sickness social security tax = 24.50",
                "Income for calculating health security tax: 862.90",
                "Health security tax: 9% = 77.66 7.75% = 66.87",
                "Tax deductible expenses = 172.58",
                "income to be taxed = 690.32 rounded = 690",
                "Advance tax 18% = 124.20",
                "Already paid tax = 124.20",
                "Advance tax = 57.33 rounded = 57",
                "",
                "Net income = 728.24");
    }

    @Test
    void handlesCivilContractType() {
        assertThat(generator.getContractType()).isEqualTo(ContractType.CIVIL);
    }

    private static TaxCalculationResult civilContractResult() {
        return TaxCalculationResult.builder()
                .contractType(ContractType.CIVIL)
                .grossIncome(new BigDecimal("1000"))
                .pensionContribution(new BigDecimal("97.60"))
                .disabilityContribution(new BigDecimal("15.00"))
                .sicknessContribution(new BigDecimal("24.50"))
                .totalSocialSecurity(new BigDecimal("137.10"))
                .healthContributionBase(new BigDecimal("862.90"))
                .healthContribution(new BigDecimal("77.66"))
                .taxDeductibleHealthContribution(new BigDecimal("66.87"))
                .taxDeductibleExpenses(new BigDecimal("172.58"))
                .taxableBase(new BigDecimal("690.32"))
                .roundedTaxableBase(new BigDecimal("690"))
                .advanceTax(new BigDecimal("124.20"))
                .taxFreeAllowance(new BigDecimal("0.00"))
                .reducedTax(new BigDecimal("124.20"))
                .advanceTaxPaid(new BigDecimal("57.33"))
                .roundedAdvanceTaxPaid(new BigDecimal("57"))
                .netIncome(new BigDecimal("728.24"))
                .build();
    }
}
