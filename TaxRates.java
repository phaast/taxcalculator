package com.bartoszwalter.students.taxes;

/**
 * Centralized repository of statutory tax, social security, and health insurance rates.
 * Eliminates magic numbers across the tax calculation domain.
 */
public final class TaxRates {

    private TaxRates() {
        // Utility / constants class; prevent instantiation
    }

    // Social Security (ZUS) contribution rates
    public static final double PENSION_CONTRIBUTION_RATE = 9.76 / 100.0;     // 9.76%
    public static final double DISABILITY_CONTRIBUTION_RATE = 1.50 / 100.0;  // 1.50%
    public static final double SICKNESS_CONTRIBUTION_RATE = 2.45 / 100.0;    // 2.45%

    // Health insurance contribution rates
    public static final double HEALTH_CONTRIBUTION_RATE = 9.00 / 100.0;      // 9.00% paid to ZUS
    public static final double TAX_DEDUCTIBLE_HEALTH_RATE = 7.75 / 100.0;    // 7.75% deductible from tax

    // Personal Income Tax (PIT) rate
    public static final double ADVANCE_TAX_RATE = 18.0 / 100.0;              // 18.00%

    // Employment contract specific constants
    public static final double STANDARD_EMPLOYMENT_EXPENSES = 111.25;        // Fixed monthly KUP
    public static final double EMPLOYMENT_TAX_FREE_ALLOWANCE = 46.33;        // Monthly tax-reducing amount

    // Civil contract specific constants
    public static final double CIVIL_EXPENSES_PERCENTAGE = 20.0 / 100.0;     // 20% deductible expenses
    public static final double CIVIL_TAX_FREE_ALLOWANCE = 0.0;               // No tax-free allowance
}
