package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractTaxStrategy implements TaxStrategy {

    private static final int CURRENCY_SCALE = 2;
    private static final int INTEGER_SCALE = 0;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @Override
    public TaxCalculationResult calculate(BigDecimal grossIncome) {
        BigDecimal pensionContribution = applyRate(grossIncome, TaxRates.PENSION_CONTRIBUTION_RATE);
        BigDecimal disabilityContribution = applyRate(grossIncome, TaxRates.DISABILITY_CONTRIBUTION_RATE);
        BigDecimal sicknessContribution = applyRate(grossIncome, TaxRates.SICKNESS_CONTRIBUTION_RATE);

        BigDecimal healthContributionBase = grossIncome;
        BigDecimal healthContribution = applyRate(healthContributionBase, TaxRates.HEALTH_CONTRIBUTION_RATE);
        BigDecimal taxDeductibleHealthContribution = applyRate(healthContributionBase, TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE);

        BigDecimal taxDeductibleExpenses = calculateTaxDeductibleExpenses(grossIncome);
        BigDecimal taxableBase = grossIncome.subtract(taxDeductibleExpenses);
        BigDecimal roundedTaxableBase = roundToWhole(taxableBase);

        BigDecimal advanceTax = applyRate(roundedTaxableBase, TaxRates.ADVANCE_TAX_RATE);
        BigDecimal taxFreeAllowance = getTaxFreeAllowance();
        BigDecimal reducedTax = advanceTax.subtract(taxFreeAllowance);

        BigDecimal advanceTaxPaid = advanceTax.subtract(taxDeductibleHealthContribution).subtract(taxFreeAllowance);
        BigDecimal roundedAdvanceTaxPaid = roundToWhole(advanceTaxPaid);

        BigDecimal totalSocialSecurity = pensionContribution.add(disabilityContribution).add(sicknessContribution);
        BigDecimal netIncome = grossIncome.subtract(totalSocialSecurity.add(healthContribution).add(roundedAdvanceTaxPaid));

        return TaxCalculationResult.builder()
                .contractType(getContractType())
                .grossIncome(grossIncome)
                .pensionContribution(pensionContribution)
                .disabilityContribution(disabilityContribution)
                .sicknessContribution(sicknessContribution)
                .totalSocialSecurity(totalSocialSecurity)
                .healthContributionBase(healthContributionBase)
                .healthContribution(healthContribution)
                .taxDeductibleHealthContribution(taxDeductibleHealthContribution)
                .taxDeductibleExpenses(taxDeductibleExpenses)
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

    protected abstract BigDecimal calculateTaxDeductibleExpenses(BigDecimal grossIncome);

    protected abstract BigDecimal getTaxFreeAllowance();

    protected BigDecimal applyRate(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(CURRENCY_SCALE, ROUNDING_MODE);
    }

    protected BigDecimal roundToWhole(BigDecimal amount) {
        return amount.setScale(INTEGER_SCALE, ROUNDING_MODE);
    }
}
