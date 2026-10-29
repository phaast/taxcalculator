package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class AbstractTaxStrategy implements TaxStrategy {

    private static final int CURRENCY_SCALE = 2;
    private static final int INTEGER_SCALE = 0;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    public TaxCalculationResult calculate(BigDecimal grossIncome) {
        BigDecimal pensionContribution = applyRate(grossIncome, TaxRates.PENSION_CONTRIBUTION_RATE);
        BigDecimal disabilityContribution = applyRate(grossIncome, TaxRates.DISABILITY_CONTRIBUTION_RATE);
        BigDecimal sicknessContribution = applyRate(grossIncome, TaxRates.SICKNESS_CONTRIBUTION_RATE);
        BigDecimal totalSocialSecurity = pensionContribution.add(disabilityContribution).add(sicknessContribution);

        BigDecimal incomeAfterSocialSecurity = grossIncome.subtract(totalSocialSecurity);
        BigDecimal healthContribution = applyRate(incomeAfterSocialSecurity, TaxRates.HEALTH_CONTRIBUTION_RATE);
        BigDecimal taxDeductibleHealthContribution = applyRate(incomeAfterSocialSecurity, TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE);

        BigDecimal taxDeductibleExpenses = calculateTaxDeductibleExpenses(incomeAfterSocialSecurity);
        BigDecimal taxableBase = nonNegative(incomeAfterSocialSecurity.subtract(taxDeductibleExpenses));
        BigDecimal roundedTaxableBase = roundToWhole(taxableBase);

        BigDecimal advanceTax = applyRate(roundedTaxableBase, TaxRates.ADVANCE_TAX_RATE);
        BigDecimal taxFreeAllowance = getTaxFreeAllowance();
        BigDecimal reducedTax = nonNegative(advanceTax.subtract(taxFreeAllowance));

        BigDecimal advanceTaxPaid = nonNegative(reducedTax.subtract(taxDeductibleHealthContribution));
        BigDecimal roundedAdvanceTaxPaid = roundToWhole(advanceTaxPaid);

        BigDecimal netIncome = incomeAfterSocialSecurity.subtract(healthContribution).subtract(roundedAdvanceTaxPaid);

        return TaxCalculationResult.builder()
                .contractType(getContractType())
                .grossIncome(grossIncome)
                .pensionContribution(pensionContribution)
                .disabilityContribution(disabilityContribution)
                .sicknessContribution(sicknessContribution)
                .totalSocialSecurity(totalSocialSecurity)
                .healthContributionBase(incomeAfterSocialSecurity)
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

    protected abstract BigDecimal calculateTaxDeductibleExpenses(BigDecimal incomeAfterSocialSecurity);

    protected abstract BigDecimal getTaxFreeAllowance();

    protected BigDecimal applyRate(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(CURRENCY_SCALE, ROUNDING_MODE);
    }

    protected BigDecimal roundToWhole(BigDecimal amount) {
        return amount.setScale(INTEGER_SCALE, ROUNDING_MODE);
    }

    private BigDecimal nonNegative(BigDecimal amount) {
        return amount.max(BigDecimal.ZERO);
    }
}
