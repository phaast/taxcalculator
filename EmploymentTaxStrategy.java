package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;

public class EmploymentTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.EMPLOYMENT;
    }

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal grossIncome) {
        return TaxRates.STANDARD_EMPLOYMENT_EXPENSES;
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.EMPLOYMENT_TAX_FREE_ALLOWANCE;
    }
}
