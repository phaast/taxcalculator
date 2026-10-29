package com.bartoszwalter.students.taxes.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EmploymentReportGeneratorTest {

    private final TaxReportGenerator generator = new EmploymentReportGenerator();

    @Test
    void generatesEmploymentReport() {
        assertThat(generator.generate(employmentResult()).getLines()).containsExactly(
                "EMPLOYMENT",
                "Income 1000.00",
                "Social security tax 97.60",
                "Disability social security tax 15.00",
                "Sickness social security tax 24.50",
                "Income basis for health social security: 862.90",
                "Health social security tax: 9% = 77.66 7.75% = 66.87",
                "Tax deductible expenses 111.25",
                "income 751.65 rounded 752",
                "Advance tax 18% = 135.36",
                "Tax free income = 46.33",
                "Reduced tax = 89.03",
                "Advance tax paid = 22.16 rounded = 22",
                "",
                "Net income = 763.24");
    }

    @Test
    void handlesEmploymentContractType() {
        assertThat(generator.getContractType()).isEqualTo(ContractType.EMPLOYMENT);
    }

    private static TaxCalculationResult employmentResult() {
        return TaxCalculationResult.builder()
                .contractType(ContractType.EMPLOYMENT)
                .grossIncome(new BigDecimal("1000"))
                .pensionContribution(new BigDecimal("97.60"))
                .disabilityContribution(new BigDecimal("15.00"))
                .sicknessContribution(new BigDecimal("24.50"))
                .totalSocialSecurity(new BigDecimal("137.10"))
                .healthContributionBase(new BigDecimal("862.90"))
                .healthContribution(new BigDecimal("77.66"))
                .taxDeductibleHealthContribution(new BigDecimal("66.87"))
                .taxDeductibleExpenses(new BigDecimal("111.25"))
                .taxableBase(new BigDecimal("751.65"))
                .roundedTaxableBase(new BigDecimal("752"))
                .advanceTax(new BigDecimal("135.36"))
                .taxFreeAllowance(new BigDecimal("46.33"))
                .reducedTax(new BigDecimal("89.03"))
                .advanceTaxPaid(new BigDecimal("22.16"))
                .roundedAdvanceTaxPaid(new BigDecimal("22"))
                .netIncome(new BigDecimal("763.24"))
                .build();
    }
}
