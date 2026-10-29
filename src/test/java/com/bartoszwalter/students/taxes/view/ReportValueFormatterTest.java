package com.bartoszwalter.students.taxes.view;

import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatAmount;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatRate;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatWholeAmount;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReportValueFormatterTest {

    @ParameterizedTest
    @CsvSource({"1000, 1000.00", "0.5, 0.50", "97.6, 97.60", "66.875, 66.88"})
    void formatsAmountWithTwoDecimalPlaces(BigDecimal amount, String expectedText) {
        assertThat(formatAmount(amount)).isEqualTo(expectedText);
    }

    @ParameterizedTest
    @CsvSource({"752, 752", "752.50, 753", "22.16, 22"})
    void formatsWholeAmountRoundedHalfUp(BigDecimal amount, String expectedText) {
        assertThat(formatWholeAmount(amount)).isEqualTo(expectedText);
    }

    @ParameterizedTest
    @CsvSource({"0.0900, 9%", "0.0775, 7.75%", "0.18, 18%", "0.0976, 9.76%"})
    void formatsRateAsPercentage(BigDecimal rate, String expectedText) {
        assertThat(formatRate(rate)).isEqualTo(expectedText);
    }

    @Test
    void formatsAmountWithDotSeparatorRegardlessOfDefaultLocale() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("pl-PL"));

            assertThat(formatAmount(new BigDecimal("97.6"))).isEqualTo("97.60");
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}
