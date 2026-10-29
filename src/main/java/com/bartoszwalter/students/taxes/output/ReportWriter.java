package com.bartoszwalter.students.taxes.output;

import com.bartoszwalter.students.taxes.view.TaxReport;

public interface ReportWriter {

    void write(TaxReport report);
}
