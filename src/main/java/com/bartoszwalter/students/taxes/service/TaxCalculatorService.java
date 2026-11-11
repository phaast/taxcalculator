package com.bartoszwalter.students.taxes.service;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.ContractTypeRegistry;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.util.Collection;

public class TaxCalculatorService {

    private final ContractTypeRegistry<TaxStrategy> strategies;

    public TaxCalculatorService(Collection<? extends TaxStrategy> strategies) {
        this.strategies = new ContractTypeRegistry<>(strategies);
    }

    public TaxCalculationResult calculate(ContractType contractType, BigDecimal grossIncome) {
        if (grossIncome == null || grossIncome.signum() < 0) {
            throw new IllegalArgumentException("Income cannot be negative or null");
        }

        return strategies.get(contractType).calculate(grossIncome);
    }
}
