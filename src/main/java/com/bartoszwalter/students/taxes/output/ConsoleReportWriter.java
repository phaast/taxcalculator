package com.bartoszwalter.students.taxes.output;

import com.bartoszwalter.students.taxes.view.TaxReport;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsoleReportWriter implements ReportWriter {

    private final PrintStream out;

    @Override
    public void write(TaxReport report) {
        report.getLines().forEach(out::println);
    }
}
