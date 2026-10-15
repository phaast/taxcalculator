package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;

public class CivilContractTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal grossIncome) {
        return applyRate(grossIncome, TaxRates.CIVIL_EXPENSES_PERCENTAGE);
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.CIVIL_TAX_FREE_ALLOWANCE;
    }
}
