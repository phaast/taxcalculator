package com.bartoszwalter.students.taxes;

import java.math.BigDecimal;

public final class TaxRates {

    private TaxRates() {
    }

    public static final BigDecimal PENSION_CONTRIBUTION_RATE = new BigDecimal("0.0976");
    public static final BigDecimal DISABILITY_CONTRIBUTION_RATE = new BigDecimal("0.0150");
    public static final BigDecimal SICKNESS_CONTRIBUTION_RATE = new BigDecimal("0.0245");

    public static final BigDecimal HEALTH_CONTRIBUTION_RATE = new BigDecimal("0.0900");
    public static final BigDecimal TAX_DEDUCTIBLE_HEALTH_RATE = new BigDecimal("0.0775");

    public static final BigDecimal ADVANCE_TAX_RATE = new BigDecimal("0.18");

    public static final BigDecimal STANDARD_EMPLOYMENT_EXPENSES = new BigDecimal("111.25");
    public static final BigDecimal EMPLOYMENT_TAX_FREE_ALLOWANCE = new BigDecimal("46.33");

    public static final BigDecimal CIVIL_EXPENSES_PERCENTAGE = new BigDecimal("0.20");
    public static final BigDecimal CIVIL_TAX_FREE_ALLOWANCE = new BigDecimal("0.00");
}
