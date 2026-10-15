package com.bartoszwalter.students.taxes;

import java.text.DecimalFormat;

public abstract class AbstractTaxStrategy implements TaxStrategy {

    private final DecimalFormat integerFormat = new DecimalFormat("#");

    @Override
    public TaxCalculationResult calculate(double grossIncome) {
        double pensionContribution = grossIncome * TaxRates.PENSION_CONTRIBUTION_RATE;
        double disabilityContribution = grossIncome * TaxRates.DISABILITY_CONTRIBUTION_RATE;
        double sicknessContribution = grossIncome * TaxRates.SICKNESS_CONTRIBUTION_RATE;

        double healthContributionBase = grossIncome;
        double healthContribution = healthContributionBase * TaxRates.HEALTH_CONTRIBUTION_RATE;
        double taxDeductibleHealthContribution = healthContributionBase * TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE;

        double taxDeductibleExpenses = calculateTaxDeductibleExpenses(grossIncome);
        double taxableBase = grossIncome - taxDeductibleExpenses;
        double roundedTaxableBase = round(taxableBase);

        double advanceTax = (roundedTaxableBase * 18.0) / 100.0;
        double taxFreeAllowance = getTaxFreeAllowance();
        double reducedTax = advanceTax - taxFreeAllowance;

        double advanceTaxPaid = advanceTax - taxDeductibleHealthContribution - taxFreeAllowance;
        double roundedAdvanceTaxPaid = round(advanceTaxPaid);

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

    protected abstract double calculateTaxDeductibleExpenses(double grossIncome);

    protected abstract double getTaxFreeAllowance();

    protected double round(double value) {
        return Double.parseDouble(integerFormat.format(value));
    }
}
