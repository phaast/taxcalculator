package com.bartoszwalter.students.taxes;

public class CivilContractTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    protected double calculateTaxDeductibleExpenses(double grossIncome) {
        return grossIncome * TaxRates.CIVIL_EXPENSES_PERCENTAGE;
    }

    @Override
    protected double getTaxFreeAllowance() {
        return TaxRates.CIVIL_TAX_FREE_ALLOWANCE;
    }
}
