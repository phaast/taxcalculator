package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;

public interface TaxStrategy {

    ContractType getContractType();

    TaxCalculationResult calculate(BigDecimal grossIncome);
}
