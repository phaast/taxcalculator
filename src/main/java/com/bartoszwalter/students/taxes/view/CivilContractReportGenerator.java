package com.bartoszwalter.students.taxes.view;

import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatAmount;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatRate;
import static com.bartoszwalter.students.taxes.view.ReportValueFormatter.formatWholeAmount;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxRates;
import java.util.List;

public class CivilContractReportGenerator implements TaxReportGenerator {

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    public TaxReport generate(TaxCalculationResult result) {
        return new TaxReport(List.of(
                "income " + formatAmount(result.getGrossIncome()),
                "Social security tax = " + formatAmount(result.getPensionContribution()),
                "Disability social security tax = " + formatAmount(result.getDisabilityContribution()),
                "Sickness social security tax = " + formatAmount(result.getSicknessContribution()),
                "Income for calculating health security tax: " + formatAmount(result.getHealthContributionBase()),
                "Health security tax: "
                        + formatRate(TaxRates.HEALTH_CONTRIBUTION_RATE) + " = " + formatAmount(result.getHealthContribution())
                        + " " + formatRate(TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE) + " = "
                        + formatAmount(result.getTaxDeductibleHealthContribution()),
                "Tax deductible expenses = " + formatAmount(result.getTaxDeductibleExpenses()),
                "income to be taxed = " + formatAmount(result.getTaxableBase())
                        + " rounded = " + formatWholeAmount(result.getRoundedTaxableBase()),
                "Advance tax " + formatRate(TaxRates.ADVANCE_TAX_RATE) + " = " + formatAmount(result.getAdvanceTax()),
                "Already paid tax = " + formatAmount(result.getReducedTax()),
                "Advance tax = " + formatAmount(result.getAdvanceTaxPaid())
                        + " rounded = " + formatWholeAmount(result.getRoundedAdvanceTaxPaid()),
                "",
                "Net income = " + formatAmount(result.getNetIncome())));
    }
}
