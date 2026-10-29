package com.bartoszwalter.students.taxes.view;

import com.bartoszwalter.students.taxes.model.ContractTypeAware;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;

public interface TaxReportGenerator extends ContractTypeAware {

    TaxReport generate(TaxCalculationResult result);
}
