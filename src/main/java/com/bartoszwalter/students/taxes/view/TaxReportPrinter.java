package com.bartoszwalter.students.taxes.view;

import com.bartoszwalter.students.taxes.model.ContractTypeAware;
import com.bartoszwalter.students.taxes.model.TaxCalculationResult;

public interface TaxReportPrinter extends ContractTypeAware {

    void print(TaxCalculationResult result);
}
