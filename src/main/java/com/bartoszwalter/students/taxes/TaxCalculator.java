package com.bartoszwalter.students.taxes;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import com.bartoszwalter.students.taxes.view.TaxReportPrinter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class TaxCalculator {

    public static void main(String[] args) {
        try {
            InputData input = readInput();

            TaxCalculationResult result = createCalculatorService().calculate(input.getContractType(), input.getIncome());

            TaxReportPrinter printer = new TaxReportPrinter();
            printer.printReport(result);

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

    private static InputData readInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter income: ");
        BigDecimal incomeInput = new BigDecimal(reader.readLine().trim());

        System.out.print("Contract Type: (E)mployment, (C)ivil: ");
        char contractCode = reader.readLine().trim().charAt(0);

        ContractType type = ContractType.fromCode(contractCode);
        return new InputData(incomeInput, type);
    }

    @Getter
    @AllArgsConstructor
    private static class InputData {
        private final BigDecimal income;
        private final ContractType contractType;
    }
}
