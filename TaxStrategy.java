package com.bartoszwalter.students.taxes;

public interface TaxStrategy {

    ContractType getContractType();

    TaxCalculationResult calculate(double grossIncome);
}
