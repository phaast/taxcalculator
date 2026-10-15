package com.bartoszwalter.students.taxes;

import java.text.DecimalFormat;

/**
 * Base template strategy containing shared tax calculation formulas.
 * Subclasses supply contract-specific deductible expenses and tax-free allowances.
 */
public abstract class AbstractTaxStrategy implements TaxStrategy {

    private final DecimalFormat integerFormat = new DecimalFormat("#");

    @Override
    public TaxCalculationResult calculate(double grossIncome) {
        // 1. Social security contributions
        double pensionContribution = grossIncome * TaxRates.PENSION_CONTRIBUTION_RATE;
        double disabilityContribution = grossIncome * TaxRates.DISABILITY_CONTRIBUTION_RATE;
        double sicknessContribution = grossIncome * TaxRates.SICKNESS_CONTRIBUTION_RATE;

        // 2. Health insurance contributions
        // In the legacy calculator baseline, health contributions are assessed on grossIncome
        double healthContributionBase = grossIncome;
        double healthContribution = healthContributionBase * TaxRates.HEALTH_CONTRIBUTION_RATE;
        double taxDeductibleHealthContribution = healthContributionBase * TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE;

        // 3. Tax deductible expenses & taxable base
        double taxDeductibleExpenses = calculateTaxDeductibleExpenses(grossIncome);
        double taxableBase = grossIncome - taxDeductibleExpenses;
        double roundedTaxableBase = round(taxableBase);

        // 4. Advance tax (18%) and reductions
        double advanceTax = (roundedTaxableBase * 18.0) / 100.0;
        double taxFreeAllowance = getTaxFreeAllowance();
        double reducedTax = advanceTax - taxFreeAllowance;

        // 5. Net advance tax to be paid to tax office
        double advanceTaxPaid = advanceTax - taxDeductibleHealthContribution - taxFreeAllowance;
        double roundedAdvanceTaxPaid = round(advanceTaxPaid);

        // 6. Final take-home net income
        double totalSocialSecurity = pensionContribution + disabilityContribution + sicknessContribution;
        double netIncome = grossIncome - (totalSocialSecurity + healthContribution + roundedAdvanceTaxPaid);

        return new TaxCalculationResult(
                getContractType(),
                grossIncome,
                pensionContribution,
                disabilityContribution,
                sicknessContribution,
                healthContributionBase,
                healthContribution,
                taxDeductibleHealthContribution,
                taxDeductibleExpenses,
                taxableBase,
                roundedTaxableBase,
                advanceTax,
                taxFreeAllowance,
                reducedTax,
                advanceTaxPaid,
                roundedAdvanceTaxPaid,
                netIncome
        );
    }

    /**
     * Contract-specific tax-deductible expenses (Koszty Uzyskania Przychodu).
     */
    protected abstract double calculateTaxDeductibleExpenses(double grossIncome);

    /**
     * Contract-specific monthly tax-free reducing allowance (Kwota Zmniejszająca Podatek).
     */
    protected abstract double getTaxFreeAllowance();

    /**
     * Rounds value to the nearest whole integer using standard half-even rounding,
     * maintaining exact backwards compatibility with DecimalFormat("#").
     */
    protected double round(double value) {
        return Double.parseDouble(integerFormat.format(value));
    }
}
