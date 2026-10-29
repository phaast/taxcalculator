package com.bartoszwalter.students.taxes;

import com.bartoszwalter.students.taxes.input.ConsoleTaxInputReader;
import com.bartoszwalter.students.taxes.model.ContractTypeRegistry;
import com.bartoszwalter.students.taxes.output.ConsoleReportWriter;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import com.bartoszwalter.students.taxes.view.CivilContractReportGenerator;
import com.bartoszwalter.students.taxes.view.EmploymentReportGenerator;
import com.bartoszwalter.students.taxes.view.TaxReportGenerator;
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
                createReportGenerators(),
                new ConsoleReportWriter(System.out));
    }

    private static TaxCalculatorService createCalculatorService() {
        return new TaxCalculatorService(List.of(
                new EmploymentTaxStrategy(),
                new CivilContractTaxStrategy()));
    }

    private static ContractTypeRegistry<TaxReportGenerator> createReportGenerators() {
        return new ContractTypeRegistry<>(List.of(
                new EmploymentReportGenerator(),
                new CivilContractReportGenerator()));
    }
}
