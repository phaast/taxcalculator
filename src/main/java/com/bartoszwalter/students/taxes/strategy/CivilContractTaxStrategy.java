package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.math.BigDecimal;

public class CivilContractTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal incomeAfterSocialSecurity) {
        return applyRate(incomeAfterSocialSecurity, TaxRates.CIVIL_EXPENSES_PERCENTAGE);
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.CIVIL_TAX_FREE_ALLOWANCE;
    }
}
