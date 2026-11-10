package com.bartoszwalter.students.taxes.report;

import java.util.List;
import lombok.Getter;

@Getter
public class TaxReport {

    private final String title;
    private final List<ReportEntry> entries;

    public TaxReport(String title, List<ReportEntry> entries) {
        this.title = title;
        this.entries = List.copyOf(entries);
    }
}
