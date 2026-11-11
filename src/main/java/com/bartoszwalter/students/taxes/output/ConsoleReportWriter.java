package com.bartoszwalter.students.taxes.output;

import static com.bartoszwalter.students.taxes.output.ReportValueFormatter.formatAmount;
import static com.bartoszwalter.students.taxes.output.ReportValueFormatter.formatRate;
import static com.bartoszwalter.students.taxes.output.ReportValueFormatter.formatWholeAmount;

import com.bartoszwalter.students.taxes.report.ReportEntry;
import com.bartoszwalter.students.taxes.report.TaxReport;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsoleReportWriter implements ReportWriter {

    private static final String LABEL_SEPARATOR = ": ";

    private final PrintStream out;

    @Override
    public void write(TaxReport report) {
        out.println(report.getTitle());
        report.getEntries().forEach(entry -> out.println(entry.getLabel() + LABEL_SEPARATOR + formatValue(entry)));
    }

    private String formatValue(ReportEntry entry) {
        return switch (entry.getFormat()) {
            case AMOUNT -> formatAmount(entry.getValue());
            case WHOLE_AMOUNT -> formatWholeAmount(entry.getValue());
            case RATE -> formatRate(entry.getValue());
        };
    }
}
