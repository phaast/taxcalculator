package com.bartoszwalter.students.taxes.view;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ReportValueFormatter {

    private static final int AMOUNT_SCALE = 2;
    private static final int WHOLE_AMOUNT_SCALE = 0;
    private static final int RATE_TO_PERCENT_SHIFT = 2;
    private static final String PERCENT_SIGN = "%";
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private ReportValueFormatter() {
    }

    public static String formatAmount(BigDecimal amount) {
        return amount.setScale(AMOUNT_SCALE, ROUNDING_MODE).toPlainString();
    }

    public static String formatWholeAmount(BigDecimal amount) {
        return amount.setScale(WHOLE_AMOUNT_SCALE, ROUNDING_MODE).toPlainString();
    }

    public static String formatRate(BigDecimal rate) {
        return rate.movePointRight(RATE_TO_PERCENT_SHIFT).stripTrailingZeros().toPlainString() + PERCENT_SIGN;
    }
}
