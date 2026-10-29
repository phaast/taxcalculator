package com.bartoszwalter.students.taxes.strategy;

import com.bartoszwalter.students.taxes.model.ContractTypeAware;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.math.BigDecimal;

public interface TaxStrategy extends ContractTypeAware {

    TaxCalculationResult calculate(BigDecimal grossIncome);
}
