package com.bartoszwalter.students.taxes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point for the Tax Calculator console application.
 * Refactored to adhere to Clean Code principles:
 * - Single Responsibility Principle (SRP): Coordinates input, calculation, and output.
 * - Meaningful naming and clean method extraction.
 * - Delegated business logic to {@link TaxCalculatorService}.
 * - Delegated presentation logic to {@link TaxReportPrinter}.
 */
public class TaxCalculator {

    // Legacy static fields preserved for backwards compatibility with external consumers
    @Deprecated public static double income = 0;
    @Deprecated public static char contractType = ' ';
    @Deprecated public static double soc_security = 0;
    @Deprecated public static double soc_health_security = 0;
    @Deprecated public static double soc_sick_security = 0;
    @Deprecated public static double taxDeductibleExpenses = 111.25;
    @Deprecated public static double soc_health1 = 0;
    @Deprecated public static double soc_health2 = 0;
    @Deprecated public static double advanceTax = 0;
    @Deprecated public static double taxFreeIncome = 46.33;
    @Deprecated public static double advanceTaxPaid = 0;
    @Deprecated public static double advanceTaxPaid0 = 0;

    public static void main(String[] args) {
        try {
            InputData input = readInput();
            
            TaxCalculatorService service = new TaxCalculatorService();
            TaxCalculationResult result = service.calculate(input.contractType, input.income);

            populateLegacyFields(result);

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

    private static void populateLegacyFields(TaxCalculationResult result) {
        income = result.getGrossIncome();
        contractType = result.getContractType().getCode();
        soc_security = result.getPensionContribution();
        soc_health_security = result.getDisabilityContribution();
        soc_sick_security = result.getSicknessContribution();
        taxDeductibleExpenses = result.getTaxDeductibleExpenses();
        soc_health1 = result.getHealthContribution();
        soc_health2 = result.getTaxDeductibleHealthContribution();
        advanceTax = result.getAdvanceTax();
        taxFreeIncome = result.getTaxFreeAllowance();
        advanceTaxPaid = result.getAdvanceTaxPaid();
        advanceTaxPaid0 = result.getRoundedAdvanceTaxPaid();
    }

    // Legacy calculation methods preserved as delegates for backwards compatibility
    @Deprecated
    public static void calculateAdvanceTax() {
        advanceTaxPaid = advanceTax - soc_health2 - taxFreeIncome;
    }

    @Deprecated
    public static void calculateTax(double taxedIncome) {
        advanceTax = (taxedIncome * 18) / 100;
    }

    @Deprecated
    public static double calculateIncome(double grossIncome) {
        soc_security = (grossIncome * 9.76) / 100;
        soc_health_security = (grossIncome * 1.5) / 100;
        soc_sick_security = (grossIncome * 2.45) / 100;
        return grossIncome - soc_security - soc_health_security - soc_sick_security;
    }

    @Deprecated
    public static void calculateOtherTaxes(double basis) {
        soc_health1 = (basis * 9) / 100;
        soc_health2 = (basis * 7.75) / 100;
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
