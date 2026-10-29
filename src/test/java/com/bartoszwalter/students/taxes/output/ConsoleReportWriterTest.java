package com.bartoszwalter.students.taxes.output;

import static org.assertj.core.api.Assertions.assertThat;

import com.bartoszwalter.students.taxes.view.TaxReport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConsoleReportWriterTest {

    @Test
    void writesEveryReportLineInOrder() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ReportWriter writer = new ConsoleReportWriter(new PrintStream(output, true, StandardCharsets.UTF_8));

        writer.write(new TaxReport(List.of("EMPLOYMENT", "", "Net income = 763.24")));

        assertThat(output.toString(StandardCharsets.UTF_8).lines())
                .containsExactly("EMPLOYMENT", "", "Net income = 763.24");
    }
}
