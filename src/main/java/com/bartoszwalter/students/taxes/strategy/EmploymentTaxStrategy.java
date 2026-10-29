package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.math.BigDecimal;

public class EmploymentTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.EMPLOYMENT;
    }

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal incomeAfterSocialSecurity) {
        return TaxRates.STANDARD_EMPLOYMENT_EXPENSES;
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.EMPLOYMENT_TAX_FREE_ALLOWANCE;
    }
}
