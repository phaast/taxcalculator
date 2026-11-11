package com.bartoszwalter.students.taxes.model;

import static com.bartoszwalter.students.taxes.report.ValueFormat.AMOUNT;
import static com.bartoszwalter.students.taxes.report.ValueFormat.RATE;
import static com.bartoszwalter.students.taxes.report.ValueFormat.WHOLE_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.bartoszwalter.students.taxes.report.ReportEntry;
import com.bartoszwalter.students.taxes.report.TaxReport;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TaxCalculationResultTest {

    @Test
    void describesEveryCalculationStepInReport() {
        TaxReport report = resultFor(ContractType.EMPLOYMENT).toReport();

        assertThat(report.getEntries())
                .extracting(ReportEntry::getLabel, ReportEntry::getValue, ReportEntry::getFormat)
                .containsExactly(
                        tuple("Gross income", new BigDecimal("1000.00"), AMOUNT),
                        tuple("Pension contribution", new BigDecimal("97.60"), AMOUNT),
                        tuple("Disability contribution", new BigDecimal("15.00"), AMOUNT),
                        tuple("Sickness contribution", new BigDecimal("24.50"), AMOUNT),
                        tuple("Total social security contributions", new BigDecimal("137.10"), AMOUNT),
                        tuple("Health contribution base", new BigDecimal("862.90"), AMOUNT),
                        tuple("Health contribution rate", TaxRates.HEALTH_CONTRIBUTION_RATE, RATE),
                        tuple("Health contribution", new BigDecimal("77.66"), AMOUNT),
                        tuple("Tax-deductible health contribution rate", TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE, RATE),
                        tuple("Tax-deductible health contribution", new BigDecimal("66.87"), AMOUNT),
                        tuple("Tax-deductible expenses", new BigDecimal("111.25"), AMOUNT),
                        tuple("Taxable base", new BigDecimal("751.65"), AMOUNT),
                        tuple("Taxable base (rounded)", new BigDecimal("752"), WHOLE_AMOUNT),
                        tuple("Advance tax rate", TaxRates.ADVANCE_TAX_RATE, RATE),
                        tuple("Advance tax", new BigDecimal("135.36"), AMOUNT),
                        tuple("Tax-free allowance", new BigDecimal("46.33"), AMOUNT),
                        tuple("Tax after allowance", new BigDecimal("89.03"), AMOUNT),
                        tuple("Advance tax payable", new BigDecimal("22.16"), AMOUNT),
                        tuple("Advance tax payable (rounded)", new BigDecimal("22"), WHOLE_AMOUNT),
                        tuple("Net income", new BigDecimal("763.24"), AMOUNT));
    }

    @ParameterizedTest
    @EnumSource(ContractType.class)
    void titlesReportWithContractType(ContractType contractType) {
        assertThat(resultFor(contractType).toReport().getTitle()).isEqualTo(contractType.getDescription());
    }

    private static TaxCalculationResult resultFor(ContractType contractType) {
        return TaxCalculationResult.builder()
                .contractType(contractType)
                .grossIncome(new BigDecimal("1000.00"))
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
