package com.bartoszwalter.students.taxes.view;

import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatAmount;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatRate;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatWholeAmount;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CivilContractReportPrinter implements TaxReportPrinter {

    private final PrintStream out;

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    public void print(TaxCalculationResult result) {
        out.println("income " + formatAmount(result.getGrossIncome()));
        out.println("Social security tax = " + formatAmount(result.getPensionContribution()));
        out.println("Disability social security tax = " + formatAmount(result.getDisabilityContribution()));
        out.println("Sickness social security tax = " + formatAmount(result.getSicknessContribution()));
        out.println("Income for calculating health security tax: " + formatAmount(result.getHealthContributionBase()));
        out.println("Health security tax: "
                + formatRate(TaxRates.HEALTH_CONTRIBUTION_RATE) + " = " + formatAmount(result.getHealthContribution())
                + " " + formatRate(TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE) + " = "
                + formatAmount(result.getTaxDeductibleHealthContribution()));
        out.println("Tax deductible expenses = " + formatAmount(result.getTaxDeductibleExpenses()));
        out.println("income to be taxed = " + formatAmount(result.getTaxableBase())
                + " rounded = " + formatWholeAmount(result.getRoundedTaxableBase()));
        out.println("Advance tax " + formatRate(TaxRates.ADVANCE_TAX_RATE) + " = " + formatAmount(result.getAdvanceTax()));
        out.println("Already paid tax = " + formatAmount(result.getReducedTax()));
        out.println("Advance tax = " + formatAmount(result.getAdvanceTaxPaid())
                + " rounded = " + formatWholeAmount(result.getRoundedAdvanceTaxPaid()));
        out.println();
        out.println("Net income = " + formatAmount(result.getNetIncome()));
    }
}
