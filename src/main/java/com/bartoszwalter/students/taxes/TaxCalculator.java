package com.bartoszwalter.students.taxes;

import com.bartoszwalter.students.taxes.input.ConsoleTaxInputReader;
import com.bartoszwalter.students.taxes.output.ConsoleReportWriter;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.TaxStrategies;
import java.util.List;

public class TaxCalculator {

    public static void main(String[] args) {
        try {
            createApplication().run();
        } catch (Exception ex) {
            System.out.println("Incorrect");
            System.err.println(ex);
        }
    }

    private static TaxCalculatorApplication createApplication() {
        return new TaxCalculatorApplication(
                new ConsoleTaxInputReader(System.in, System.out),
                createCalculatorService(),
                new ConsoleReportWriter(System.out));
    }

    private static TaxCalculatorService createCalculatorService() {
        return new TaxCalculatorService(List.of(
                TaxStrategies.employment(),
                TaxStrategies.civilContract()));
    }
}
