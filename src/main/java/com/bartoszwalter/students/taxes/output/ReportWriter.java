package com.bartoszwalter.students.taxes.output;

import com.bartoszwalter.students.taxes.report.TaxReport;

public interface ReportWriter {

    void write(TaxReport report);
}
