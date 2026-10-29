package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;

public interface TaxStrategy {

    ContractType getContractType();

    TaxCalculationResult calculate(BigDecimal grossIncome);
}
