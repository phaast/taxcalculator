package com.bartoszwalter.students.taxes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.ContractTypeRegistry;
import com.bartoszwalter.students.taxes.model.TaxInput;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import com.bartoszwalter.students.taxes.view.CivilContractReportGenerator;
import com.bartoszwalter.students.taxes.view.EmploymentReportGenerator;
import com.bartoszwalter.students.taxes.view.TaxReport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TaxCalculatorApplicationTest {

    private final List<TaxReport> writtenReports = new ArrayList<>();

    @Test
    void writesEmploymentReportForEmploymentInput() {
        applicationFor(new TaxInput(new BigDecimal("1000.00"), ContractType.EMPLOYMENT)).run();

        assertThat(writtenReports).singleElement()
                .satisfies(report -> assertThat(report.getLines())
                        .startsWith("EMPLOYMENT")
                        .endsWith("Net income = 763.24"));
    }

    @Test
    void writesCivilContractReportForCivilInput() {
        applicationFor(new TaxInput(new BigDecimal("1000.00"), ContractType.CIVIL)).run();

        assertThat(writtenReports).singleElement()
                .satisfies(report -> assertThat(report.getLines())
                        .startsWith("income 1000.00")
                        .endsWith("Net income = 728.24"));
    }

    @Test
    void writesNothingWhenCalculationFails() {
        TaxCalculatorApplication application = applicationFor(new TaxInput(new BigDecimal("-1"), ContractType.EMPLOYMENT));

        assertThatThrownBy(application::run).isInstanceOf(IllegalArgumentException.class);
        assertThat(writtenReports).isEmpty();
    }

    private TaxCalculatorApplication applicationFor(TaxInput input) {
        return new TaxCalculatorApplication(
                () -> input,
                new TaxCalculatorService(List.of(new EmploymentTaxStrategy(), new CivilContractTaxStrategy())),
                new ContractTypeRegistry<>(List.of(new EmploymentReportGenerator(), new CivilContractReportGenerator())),
                writtenReports::add);
    }
}
