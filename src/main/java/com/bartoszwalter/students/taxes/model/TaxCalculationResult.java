package com.bartoszwalter.students.taxes.model;

import static com.bartoszwalter.students.taxes.report.ReportEntry.amount;
import static com.bartoszwalter.students.taxes.report.ReportEntry.rate;
import static com.bartoszwalter.students.taxes.report.ReportEntry.wholeAmount;

import com.bartoszwalter.students.taxes.report.TaxReport;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TaxCalculationResult {

    private final ContractType contractType;
    private final BigDecimal grossIncome;

    private final BigDecimal pensionContribution;
    private final BigDecimal disabilityContribution;
    private final BigDecimal sicknessContribution;
    private final BigDecimal totalSocialSecurity;

    private final BigDecimal healthContributionBase;
    private final BigDecimal healthContribution;
    private final BigDecimal taxDeductibleHealthContribution;

    private final BigDecimal taxDeductibleExpenses;
    private final BigDecimal taxableBase;
    private final BigDecimal roundedTaxableBase;

    private final BigDecimal advanceTax;
    private final BigDecimal taxFreeAllowance;
    private final BigDecimal reducedTax;
    private final BigDecimal advanceTaxPaid;
    private final BigDecimal roundedAdvanceTaxPaid;

    private final BigDecimal netIncome;

    public TaxReport toReport() {
        return new TaxReport(contractType.getDescription(), List.of(
                amount("Gross income", grossIncome),
                amount("Pension contribution", pensionContribution),
                amount("Disability contribution", disabilityContribution),
                amount("Sickness contribution", sicknessContribution),
                amount("Total social security contributions", totalSocialSecurity),
                amount("Health contribution base", healthContributionBase),
                rate("Health contribution rate", TaxRates.HEALTH_CONTRIBUTION_RATE),
                amount("Health contribution", healthContribution),
                rate("Tax-deductible health contribution rate", TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE),
                amount("Tax-deductible health contribution", taxDeductibleHealthContribution),
                amount("Tax-deductible expenses", taxDeductibleExpenses),
                amount("Taxable base", taxableBase),
                wholeAmount("Taxable base (rounded)", roundedTaxableBase),
                rate("Advance tax rate", TaxRates.ADVANCE_TAX_RATE),
                amount("Advance tax", advanceTax),
                amount("Tax-free allowance", taxFreeAllowance),
                amount("Tax after allowance", reducedTax),
                amount("Advance tax payable", advanceTaxPaid),
                wholeAmount("Advance tax payable (rounded)", roundedAdvanceTaxPaid),
                amount("Net income", netIncome)));
    }
}
