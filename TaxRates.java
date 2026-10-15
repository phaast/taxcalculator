package com.bartoszwalter.students.taxes;

public final class TaxRates {

    private TaxRates() {
    }

    public static final double PENSION_CONTRIBUTION_RATE = 9.76 / 100.0;
    public static final double DISABILITY_CONTRIBUTION_RATE = 1.50 / 100.0;
    public static final double SICKNESS_CONTRIBUTION_RATE = 2.45 / 100.0;

    public static final double HEALTH_CONTRIBUTION_RATE = 9.00 / 100.0;
    public static final double TAX_DEDUCTIBLE_HEALTH_RATE = 7.75 / 100.0;

    public static final double ADVANCE_TAX_RATE = 18.0 / 100.0;

    public static final double STANDARD_EMPLOYMENT_EXPENSES = 111.25;
    public static final double EMPLOYMENT_TAX_FREE_ALLOWANCE = 46.33;

    public static final double CIVIL_EXPENSES_PERCENTAGE = 20.0 / 100.0;
    public static final double CIVIL_TAX_FREE_ALLOWANCE = 0.0;
}
