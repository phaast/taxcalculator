package com.bartoszwalter.students.taxes;

import com.bartoszwalter.students.taxes.input.TaxInputReader;
import com.bartoszwalter.students.taxes.model.ContractTypeRegistry;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;
import com.bartoszwalter.students.taxes.model.TaxInput;
import com.bartoszwalter.students.taxes.output.ReportWriter;
import com.bartoszwalter.students.taxes.service.TaxCalculatorService;
import com.bartoszwalter.students.taxes.view.TaxReportGenerator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaxCalculatorApplication {

    private final TaxInputReader inputReader;
    private final TaxCalculatorService calculatorService;
    private final ContractTypeRegistry<TaxReportGenerator> reportGenerators;
    private final ReportWriter reportWriter;

    public void run() {
        TaxInput input = inputReader.read();

        TaxCalculationResult result = calculatorService.calculate(input.getContractType(), input.getIncome());

        reportWriter.write(reportGenerators.get(result.getContractType()).generate(result));
    }
}
