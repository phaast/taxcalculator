package com.bartoszwalter.students.taxes.view;

import java.util.List;
import lombok.Getter;

@Getter
public class TaxReport {

    private final List<String> lines;

    public TaxReport(List<String> lines) {
        this.lines = List.copyOf(lines);
    }
}
