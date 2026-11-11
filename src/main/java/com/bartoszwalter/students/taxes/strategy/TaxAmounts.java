package com.bartoszwalter.students.taxes.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxAmounts {

    private static final int CURRENCY_SCALE = 2;
    private static final int INTEGER_SCALE = 0;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private TaxAmounts() {
    }

    public static BigDecimal applyRate(BigDecimal amount, BigDecimal rate) {
        return amount.multiply(rate).setScale(CURRENCY_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal roundToWhole(BigDecimal amount) {
        return amount.setScale(INTEGER_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal nonNegative(BigDecimal amount) {
        return amount.max(BigDecimal.ZERO);
    }
}
