package com.bartoszwalter.students.taxes.view;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import java.io.PrintStream;
import java.text.DecimalFormat;

public class TaxReportPrinter {

    private final DecimalFormat currencyFormat = new DecimalFormat("#.00");
    private final DecimalFormat integerFormat = new DecimalFormat("#");
    private final PrintStream out;

    public TaxReportPrinter() {
        this(System.out);
    }

    public TaxReportPrinter(PrintStream out) {
        this.out = out;
    }

    public void printReport(TaxCalculationResult result) {
        if (result.getContractType() == ContractType.EMPLOYMENT) {
            printEmploymentReport(result);
        } else if (result.getContractType() == ContractType.CIVIL) {
            printCivilReport(result);
        } else {
            out.println("Unknown type of contract!");
        }
    }

    private void printEmploymentReport(TaxCalculationResult result) {
        out.println("EMPLOYMENT");
        out.println("Income " + result.getGrossIncome());
        out.println("Social security tax " + currencyFormat.format(result.getPensionContribution()));
        out.println("Health social security tax    " + currencyFormat.format(result.getDisabilityContribution()));
        out.println("Sickness social security tax  " + currencyFormat.format(result.getSicknessContribution()));
        out.println("Income basis for health social security: " + result.getHealthContributionBase());
        out.println("Health social security tax: 9% = "
                + currencyFormat.format(result.getHealthContribution())
                + " 7,75% = " + currencyFormat.format(result.getTaxDeductibleHealthContribution()));
        out.println("Tax deductible expenses " + result.getTaxDeductibleExpenses());
        out.println("income " + result.getTaxableBase()
                + " rounded " + integerFormat.format(result.getRoundedTaxableBase()));
        out.println("Advance tax 18 % = " + result.getAdvanceTax());
        out.println("Tax free income = " + result.getTaxFreeAllowance());
        out.println("Reduced tax = " + currencyFormat.format(result.getReducedTax()));
        out.println("Advance tax paid = " + currencyFormat.format(result.getAdvanceTaxPaid())
                + " rounded = " + integerFormat.format(result.getRoundedAdvanceTaxPaid()));
        out.println();
        out.println("Net income = " + currencyFormat.format(result.getNetIncome()));
    }

    private void printCivilReport(TaxCalculationResult result) {
        out.println("income " + result.getGrossIncome());
        out.println("Social security tax = " + currencyFormat.format(result.getPensionContribution()));
        out.println("Health social security tax = " + currencyFormat.format(result.getDisabilityContribution()));
        out.println("Sickness social security tax = " + currencyFormat.format(result.getSicknessContribution()));
        out.println("Income for calculating health security tax: " + result.getHealthContributionBase());
        out.println("Health security tax: 9% = "
                + currencyFormat.format(result.getHealthContribution())
                + " 7,75% = " + currencyFormat.format(result.getTaxDeductibleHealthContribution()));
        out.println("Tax deductible expenses = " + result.getTaxDeductibleExpenses());
        out.println("income to be taxed = " + result.getTaxableBase()
                + " rounded = " + integerFormat.format(result.getRoundedTaxableBase()));
        out.println("Advance tax 18 % = " + result.getAdvanceTax());
        out.println("Already paid tax = " + currencyFormat.format(result.getAdvanceTax()));
        out.println("Advance tax  = " + currencyFormat.format(result.getAdvanceTaxPaid())
                + " rounded = " + integerFormat.format(result.getRoundedAdvanceTaxPaid()));
        out.println();
        out.println("Net income = " + currencyFormat.format(result.getNetIncome()));
    }
}
