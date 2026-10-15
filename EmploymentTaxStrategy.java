package com.bartoszwalter.students.taxes;

public class EmploymentTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.EMPLOYMENT;
    }

    @Override
    protected double calculateTaxDeductibleExpenses(double grossIncome) {
        return TaxRates.STANDARD_EMPLOYMENT_EXPENSES;
    }

    @Override
    protected double getTaxFreeAllowance() {
        return TaxRates.EMPLOYMENT_TAX_FREE_ALLOWANCE;
    }
}
