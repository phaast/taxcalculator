package com.bartoszwalter.students.taxes.strategy;

import static com.bartoszwalter.students.taxes.strategy.TaxAmounts.applyRate;
import static com.bartoszwalter.students.taxes.strategy.TaxAmounts.nonNegative;
import static com.bartoszwalter.students.taxes.strategy.TaxAmounts.roundToWhole;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ContractTaxStrategy implements TaxStrategy {

    @Getter
    private final ContractType contractType;
    private final TaxDeductibleExpenses taxDeductibleExpenses;
    private final BigDecimal taxFreeAllowance;

    @Override
    public TaxCalculationResult calculate(BigDecimal grossIncome) {
        BigDecimal pensionContribution = applyRate(grossIncome, TaxRates.PENSION_CONTRIBUTION_RATE);
        BigDecimal disabilityContribution = applyRate(grossIncome, TaxRates.DISABILITY_CONTRIBUTION_RATE);
        BigDecimal sicknessContribution = applyRate(grossIncome, TaxRates.SICKNESS_CONTRIBUTION_RATE);
        BigDecimal totalSocialSecurity = pensionContribution.add(disabilityContribution).add(sicknessContribution);

        BigDecimal incomeAfterSocialSecurity = grossIncome.subtract(totalSocialSecurity);
        BigDecimal healthContribution = applyRate(incomeAfterSocialSecurity, TaxRates.HEALTH_CONTRIBUTION_RATE);
        BigDecimal taxDeductibleHealthContribution = applyRate(incomeAfterSocialSecurity, TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE);

        BigDecimal deductibleExpenses = taxDeductibleExpenses.calculate(incomeAfterSocialSecurity);
        BigDecimal taxableBase = nonNegative(incomeAfterSocialSecurity.subtract(deductibleExpenses));
        BigDecimal roundedTaxableBase = roundToWhole(taxableBase);

        BigDecimal advanceTax = applyRate(roundedTaxableBase, TaxRates.ADVANCE_TAX_RATE);
        BigDecimal reducedTax = nonNegative(advanceTax.subtract(taxFreeAllowance));

        BigDecimal advanceTaxPaid = nonNegative(reducedTax.subtract(taxDeductibleHealthContribution));
        BigDecimal roundedAdvanceTaxPaid = roundToWhole(advanceTaxPaid);

        BigDecimal netIncome = incomeAfterSocialSecurity.subtract(healthContribution).subtract(roundedAdvanceTaxPaid);

        return TaxCalculationResult.builder()
                .contractType(contractType)
                .grossIncome(grossIncome)
                .pensionContribution(pensionContribution)
                .disabilityContribution(disabilityContribution)
                .sicknessContribution(sicknessContribution)
                .totalSocialSecurity(totalSocialSecurity)
                .healthContributionBase(incomeAfterSocialSecurity)
                .healthContribution(healthContribution)
                .taxDeductibleHealthContribution(taxDeductibleHealthContribution)
                .taxDeductibleExpenses(deductibleExpenses)
                .taxableBase(taxableBase)
                .roundedTaxableBase(roundedTaxableBase)
                .advanceTax(advanceTax)
                .taxFreeAllowance(taxFreeAllowance)
                .reducedTax(reducedTax)
                .advanceTaxPaid(advanceTaxPaid)
                .roundedAdvanceTaxPaid(roundedAdvanceTaxPaid)
                .netIncome(netIncome)
                .build();
    }
}
