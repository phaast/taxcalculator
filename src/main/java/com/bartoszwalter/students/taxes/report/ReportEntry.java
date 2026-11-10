package com.bartoszwalter.students.taxes.report;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportEntry {

    private final String label;
    private final BigDecimal value;
    private final ValueFormat format;

    public static ReportEntry amount(String label, BigDecimal value) {
        return new ReportEntry(label, value, ValueFormat.AMOUNT);
    }

    public static ReportEntry wholeAmount(String label, BigDecimal value) {
        return new ReportEntry(label, value, ValueFormat.WHOLE_AMOUNT);
    }

    public static ReportEntry rate(String label, BigDecimal value) {
        return new ReportEntry(label, value, ValueFormat.RATE);
    }
}
