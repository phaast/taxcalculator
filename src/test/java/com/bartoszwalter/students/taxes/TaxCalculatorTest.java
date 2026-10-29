package com.bartoszwalter.students.taxes;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TaxCalculatorTest {

    private static final String PROMPTS = "Enter income: Contract Type: (E)mployment, (C)ivil: ";

    @Test
    void printsEmploymentReportForEmploymentContract() {
        List<String> output = runWithInput("1000\nE\n").lines().toList();

        assertThat(output).first().isEqualTo(PROMPTS + "EMPLOYMENT");
        assertThat(output).last().isEqualTo("Net income = 763.24");
    }

    @Test
    void printsCivilContractReportForCivilContract() {
        List<String> output = runWithInput("1000\nc\n").lines().toList();

        assertThat(output).first().isEqualTo(PROMPTS + "income 1000.00");
        assertThat(output).last().isEqualTo("Net income = 728.24");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc\nE\n", "-100\nE\n", "1000\nZ\n", "1000\n\n"})
    void printsIncorrectForInvalidInput(String input) {
        assertThat(runWithInput(input).strip()).endsWith("Incorrect");
    }

    private static String runWithInput(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(OutputStream.nullOutputStream()));

            TaxCalculator.main(new String[0]);

            return output.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
