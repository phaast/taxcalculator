package com.bartoszwalter.students.taxes;

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

    public TaxCalculationResult calculate(ContractType contractType, double grossIncome) {
        if (grossIncome < 0) {
            throw new IllegalArgumentException("Income cannot be negative: " + grossIncome);
        }

        TaxStrategy strategy = strategies.get(contractType);
        if (strategy == null) {
            throw new IllegalArgumentException("No tax calculation strategy registered for contract type: " + contractType);
        }

        return strategy.calculate(grossIncome);
    }
}
