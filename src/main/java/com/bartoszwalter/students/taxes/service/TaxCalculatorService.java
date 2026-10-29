package com.bartoszwalter.students.taxes.service;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.TaxStrategy;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class TaxCalculatorService {

    private final Map<ContractType, TaxStrategy> strategies = new EnumMap<>(ContractType.class);

    public TaxCalculatorService() {
        registerStrategy(new EmploymentTaxStrategy());
        registerStrategy(new CivilContractTaxStrategy());
    }

    public void registerStrategy(TaxStrategy strategy) {
        strategies.put(strategy.getContractType(), strategy);
    }

    public TaxCalculationResult calculate(ContractType contractType, BigDecimal grossIncome) {
        if (grossIncome == null || grossIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income cannot be negative or null");
        }

        TaxStrategy strategy = strategies.get(contractType);
        if (strategy == null) {
            throw new IllegalArgumentException("No tax calculation strategy registered for contract type: " + contractType);
        }

        return strategy.calculate(grossIncome);
    }
}
