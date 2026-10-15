package com.bartoszwalter.students.taxes;

/**
 * Strategy interface defining tax calculation for a specific contract type.
 * Adheres to Open/Closed Principle (OCP), allowing new contract types to be added
 * without modifying existing calculation logic.
 */
public interface TaxStrategy {

    /**
     * @return the ContractType handled by this strategy.
     */
    ContractType getContractType();

    /**
     * Calculates all taxes, contributions, and net income for the given gross income.
     *
     * @param grossIncome the pre-tax gross income.
     * @return an immutable TaxCalculationResult containing the detailed breakdown.
     */
    TaxCalculationResult calculate(double grossIncome);
}
