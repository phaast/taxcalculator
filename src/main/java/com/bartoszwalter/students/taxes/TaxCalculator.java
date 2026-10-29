package com.bartoszwalter.students.taxes;

import com.bartoszwalter.students.taxes.input.ConsoleTaxInputReader;
import com.bartoszwalter.students.taxes.model.ContractTypeRegistry;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxInput;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import com.bartoszwalter.students.taxes.view.CivilContractReportPrinter;
import com.bartoszwalter.students.taxes.view.EmploymentReportPrinter;
import com.bartoszwalter.students.taxes.view.TaxReportPrinter;
import java.util.List;

public class TaxCalculator {

    public static void main(String[] args) {
        try {
            TaxInput input = new ConsoleTaxInputReader(System.in, System.out).read();

            TaxCalculationResult result = createCalculatorService().calculate(input.getContractType(), input.getIncome());

            createReportPrinters().get(result.getContractType()).print(result);

        } catch (Exception ex) {
            System.out.println("Incorrect");
            System.err.println(ex);
        }
    }

    private static TaxCalculatorService createCalculatorService() {
        return new TaxCalculatorService(List.of(
                new EmploymentTaxStrategy(),
                new CivilContractTaxStrategy()));
    }

    private static ContractTypeRegistry<TaxReportPrinter> createReportPrinters() {
        return new ContractTypeRegistry<>(List.of(
                new EmploymentReportPrinter(System.out),
                new CivilContractReportPrinter(System.out)));
    }
}
