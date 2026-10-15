package com.bartoszwalter.students.taxes;

/**
 * Strategy implementation for Employment Contracts (Umowa o Pracę).
 */
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
