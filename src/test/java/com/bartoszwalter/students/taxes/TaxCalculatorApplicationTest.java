package com.bartoszwalter.students.taxes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxInput;
import com.bartoszwalter.students.taxes.report.ReportEntry;
import com.bartoszwalter.students.taxes.report.TaxReport;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.strategy.CivilContractTaxStrategy;
import com.bartoszwalter.students.taxes.strategy.EmploymentTaxStrategy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TaxCalculatorApplicationTest {

    private final List<TaxReport> writtenReports = new ArrayList<>();

    @ParameterizedTest
    @CsvSource({"EMPLOYMENT, 763.24", "CIVIL, 728.24"})
    void writesReportOfCalculationForRequestedContractType(ContractType contractType, BigDecimal expectedNetIncome) {
        applicationFor(new TaxInput(new BigDecimal("1000.00"), contractType)).run();

        assertThat(writtenReports).singleElement().satisfies(report -> {
            assertThat(report.getTitle()).isEqualTo(contractType.getDescription());
            assertThat(report.getEntries()).last()
                    .extracting(ReportEntry::getValue)
                    .satisfies(netIncome -> assertThat(netIncome).isEqualByComparingTo(expectedNetIncome));
        });
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
                writtenReports::add);
    }
}
