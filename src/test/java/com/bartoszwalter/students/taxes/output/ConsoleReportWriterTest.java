package com.bartoszwalter.students.taxes.output;

import static com.bartoszwalter.students.taxes.report.ReportEntry.amount;
import static com.bartoszwalter.students.taxes.report.ReportEntry.rate;
import static com.bartoszwalter.students.taxes.report.ReportEntry.wholeAmount;
import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.report.TaxReport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConsoleReportWriterTest {

    @Test
    void writesTitleAndEveryEntryFormattedByItsValueFormat() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ReportWriter writer = new ConsoleReportWriter(new PrintStream(output, true, StandardCharsets.UTF_8));

        writer.write(new TaxReport("EMPLOYMENT", List.of(
                amount("Gross income", new BigDecimal("1000")),
                wholeAmount("Taxable base (rounded)", new BigDecimal("752")),
                rate("Advance tax rate", new BigDecimal("0.18")))));

        assertThat(output.toString(StandardCharsets.UTF_8).lines()).containsExactly(
                "EMPLOYMENT",
                "Gross income: 1000.00",
                "Taxable base (rounded): 752",
                "Advance tax rate: 18%");
    }
}
