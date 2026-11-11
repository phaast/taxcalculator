package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxRates;

public final class TaxStrategies {

    private TaxStrategies() {
    }

    public static TaxStrategy employment() {
        return new ContractTaxStrategy(
                ContractType.EMPLOYMENT,
                new FixedTaxDeductibleExpenses(TaxRates.STANDARD_EMPLOYMENT_EXPENSES),
                TaxRates.EMPLOYMENT_TAX_FREE_ALLOWANCE);
    }

    public static TaxStrategy civilContract() {
        return new ContractTaxStrategy(
                ContractType.CIVIL,
                new PercentageTaxDeductibleExpenses(TaxRates.CIVIL_EXPENSES_PERCENTAGE),
                TaxRates.CIVIL_TAX_FREE_ALLOWANCE);
    }
}
