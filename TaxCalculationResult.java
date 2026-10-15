package com.bartoszwalter.students.taxes;

public class TaxCalculationResult {

    private final ContractType contractType;
    private final double grossIncome;

    private final double pensionContribution;
    private final double disabilityContribution;
    private final double sicknessContribution;
    private final double totalSocialSecurity;

    private final double healthContributionBase;
    private final double healthContribution;
    private final double taxDeductibleHealthContribution;

    private final double taxDeductibleExpenses;
    private final double taxableBase;
    private final double roundedTaxableBase;

    private final double advanceTax;
    private final double taxFreeAllowance;
    private final double reducedTax;
    private final double advanceTaxPaid;
    private final double roundedAdvanceTaxPaid;

    private final double netIncome;

    public TaxCalculationResult(
            ContractType contractType,
            double grossIncome,
            double pensionContribution,
            double disabilityContribution,
            double sicknessContribution,
            double healthContributionBase,
            double healthContribution,
            double taxDeductibleHealthContribution,
            double taxDeductibleExpenses,
            double taxableBase,
            double roundedTaxableBase,
            double advanceTax,
            double taxFreeAllowance,
            double reducedTax,
            double advanceTaxPaid,
            double roundedAdvanceTaxPaid,
            double netIncome) {
        this.contractType = contractType;
        this.grossIncome = grossIncome;
        this.pensionContribution = pensionContribution;
        this.disabilityContribution = disabilityContribution;
        this.sicknessContribution = sicknessContribution;
        this.totalSocialSecurity = pensionContribution + disabilityContribution + sicknessContribution;
        this.healthContributionBase = healthContributionBase;
        this.healthContribution = healthContribution;
        this.taxDeductibleHealthContribution = taxDeductibleHealthContribution;
        this.taxDeductibleExpenses = taxDeductibleExpenses;
        this.taxableBase = taxableBase;
        this.roundedTaxableBase = roundedTaxableBase;
        this.advanceTax = advanceTax;
        this.taxFreeAllowance = taxFreeAllowance;
        this.reducedTax = reducedTax;
        this.advanceTaxPaid = advanceTaxPaid;
        this.roundedAdvanceTaxPaid = roundedAdvanceTaxPaid;
        this.netIncome = netIncome;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public double getGrossIncome() {
        return grossIncome;
    }

    public double getPensionContribution() {
        return pensionContribution;
    }

    public double getDisabilityContribution() {
        return disabilityContribution;
    }

    public double getSicknessContribution() {
        return sicknessContribution;
    }

    public double getTotalSocialSecurity() {
        return totalSocialSecurity;
    }

    public double getHealthContributionBase() {
        return healthContributionBase;
    }

    public double getHealthContribution() {
        return healthContribution;
    }

    public double getTaxDeductibleHealthContribution() {
        return taxDeductibleHealthContribution;
    }

    public double getTaxDeductibleExpenses() {
        return taxDeductibleExpenses;
    }

    public double getTaxableBase() {
        return taxableBase;
    }

    public double getRoundedTaxableBase() {
        return roundedTaxableBase;
    }

    public double getAdvanceTax() {
        return advanceTax;
    }

    public double getTaxFreeAllowance() {
        return taxFreeAllowance;
    }

    public double getReducedTax() {
        return reducedTax;
    }

    public double getAdvanceTaxPaid() {
        return advanceTaxPaid;
    }

    public double getRoundedAdvanceTaxPaid() {
        return roundedAdvanceTaxPaid;
    }

    public double getNetIncome() {
        return netIncome;
    }
}
