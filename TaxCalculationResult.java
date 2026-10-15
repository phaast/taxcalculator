package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;
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
}
