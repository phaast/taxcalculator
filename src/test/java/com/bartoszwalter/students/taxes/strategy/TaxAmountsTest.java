package com.bartoszwalter.students.taxes.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TaxAmountsTest {

    @ParameterizedTest
    @CsvSource({"1000.00, 0.0976, 97.60", "862.90, 0.0775, 66.87", "0.50, 0.01, 0.01"})
    void appliesRateRoundedHalfUpToFullGrosz(String amount, String rate, String expected) {
        assertThat(TaxAmounts.applyRate(new BigDecimal(amount), new BigDecimal(rate))).isEqualByComparingTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"751.65, 752", "752.50, 753", "752.49, 752"})
    void roundsHalfUpToFullZloty(String amount, String expected) {
        assertThat(TaxAmounts.roundToWhole(new BigDecimal(amount))).isEqualByComparingTo(expected);
    }

    @Test
    void replacesNegativeAmountWithZero() {
        assertThat(TaxAmounts.nonNegative(new BigDecimal("-12.31"))).isEqualByComparingTo("0");
        assertThat(TaxAmounts.nonNegative(new BigDecimal("12.31"))).isEqualByComparingTo("12.31");
    }
}
