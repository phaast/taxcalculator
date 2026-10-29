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
public class EmploymentReportPrinter implements TaxReportPrinter {

    private final PrintStream out;

    @Override
    public ContractType getContractType() {
        return ContractType.EMPLOYMENT;
    }

    @Override
    public void print(TaxCalculationResult result) {
        out.println("EMPLOYMENT");
        out.println("Income " + formatAmount(result.getGrossIncome()));
        out.println("Social security tax " + formatAmount(result.getPensionContribution()));
        out.println("Disability social security tax " + formatAmount(result.getDisabilityContribution()));
        out.println("Sickness social security tax " + formatAmount(result.getSicknessContribution()));
        out.println("Income basis for health social security: " + formatAmount(result.getHealthContributionBase()));
        out.println("Health social security tax: "
                + formatRate(TaxRates.HEALTH_CONTRIBUTION_RATE) + " = " + formatAmount(result.getHealthContribution())
                + " " + formatRate(TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE) + " = "
                + formatAmount(result.getTaxDeductibleHealthContribution()));
        out.println("Tax deductible expenses " + formatAmount(result.getTaxDeductibleExpenses()));
        out.println("income " + formatAmount(result.getTaxableBase())
                + " rounded " + formatWholeAmount(result.getRoundedTaxableBase()));
        out.println("Advance tax " + formatRate(TaxRates.ADVANCE_TAX_RATE) + " = " + formatAmount(result.getAdvanceTax()));
        out.println("Tax free income = " + formatAmount(result.getTaxFreeAllowance()));
        out.println("Reduced tax = " + formatAmount(result.getReducedTax()));
        out.println("Advance tax paid = " + formatAmount(result.getAdvanceTaxPaid())
                + " rounded = " + formatWholeAmount(result.getRoundedAdvanceTaxPaid()));
        out.println();
        out.println("Net income = " + formatAmount(result.getNetIncome()));
    }
}
