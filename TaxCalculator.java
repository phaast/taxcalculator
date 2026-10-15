package com.bartoszwalter.students.taxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TaxCalculator {

    public static void main(String[] args) {
        try {
            InputData input = readInput();

            TaxCalculatorService service = new TaxCalculatorService();
            TaxCalculationResult result = service.calculate(input.contractType, input.income);

            TaxReportPrinter printer = new TaxReportPrinter();
            printer.printReport(result);

        } catch (Exception ex) {
            System.out.println("Incorrect");
            System.err.println(ex);
        }
    }

    private static InputData readInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter income: ");
        double incomeInput = Double.parseDouble(reader.readLine());

        System.out.print("Contract Type: (E)mployment, (C)ivil: ");
        char contractCode = reader.readLine().charAt(0);

        ContractType type = ContractType.fromCode(contractCode);
        return new InputData(incomeInput, type);
    }

    private static class InputData {
        final double income;
        final ContractType contractType;

        InputData(double income, ContractType contractType) {
            this.income = income;
            this.contractType = contractType;
        }
    }
}
